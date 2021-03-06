package ru.er_log.stock.data.network

import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import ru.er_log.stock.data.di.inject
import ru.er_log.stock.data.network.api.ErrorResponse
import java.io.IOException

private val networkCoroutineDispatcher = Dispatchers.IO
private val networkCoroutineContext = Job()

/**
 * Выполняет сетевой вызов [networkCall] и возвращает результат [R],
 * обернутый в [NetworkResult], который позволяет оценить успешность вызова.
 *
 * Функция использует IO dispatcher и объединяет свой контекст исполнения с родительским.
 * Сделано это, чтобы иметь возможность управлять временем жизни запроса извне.
 *
 * @param R тип результата в случае успешного выполнения сетевого запроса
 * @param networkCall сетевой вызов, результат которого принимает тип [R]
 * @param moshi десериализатор
 * @return результат сетевого вызова
 */
suspend fun <R> makeRequest(
    dispatcher: CoroutineDispatcher = networkCoroutineDispatcher,
    moshi: Moshi = inject(),
    networkCall: suspend () -> R
): NetworkResult<R> = withContext(dispatcher + networkCoroutineContext) {
    try {
        val result = networkCall.invoke()
        if (result is Response<*> && !result.isSuccessful) throw HttpException(result)
        else NetworkResult.Success(result)
    } catch (t: IOException) {
        NetworkResult.Failure.NetworkError(t, "Сетевая ошибка. Проверьте подключение к сети")
    } catch (t: HttpException) {
        val code = t.code()
        val errorRaw = t.response()?.errorBody()
        val errorRawText = (try { errorRaw?.string() } catch (_: IOException) { null }) ?: "Неизвестная ошибка: " + t.localizedMessage

        val adapter = moshi.adapter(ErrorResponse::class.java)
        val errorResponse = try { adapter.fromJson(errorRawText) } catch (_: IOException) { null }

        NetworkResult.Failure.GenericError(t, code, errorResponse?.parsed ?: errorRawText)
    } catch (t: Throwable) {
        NetworkResult.Failure.GenericError(t, -1, "Неизвестная ошибка: " + t.localizedMessage)
    }
}

/**
 * Результат сетевого запроса.
 */
sealed class NetworkResult<out R> {
    data class Success<out R>(val value: R) : NetworkResult<R>()

    sealed class Failure(open val t: Throwable, open val errorMessage: String) :
        NetworkResult<Nothing>() {
        /** Ошибка при HTTP-ответе, код которого не входит в диапазон [200..300). */
        data class GenericError(
            override val t: Throwable,
            val httpCode: Int,
            override val errorMessage: String
        ) : Failure(t, errorMessage)

        /** Сетевая ошибка. Соединение не удалось. */
        data class NetworkError(override val t: Throwable, override val errorMessage: String) :
            Failure(t, errorMessage)
    }
}