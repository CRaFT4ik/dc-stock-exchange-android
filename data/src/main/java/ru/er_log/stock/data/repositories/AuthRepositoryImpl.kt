package ru.er_log.stock.data.repositories

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import ru.er_log.stock.data.network.NetworkResult
import ru.er_log.stock.data.network.api.v1.auth.AuthService
import ru.er_log.stock.data.network.api.v1.auth.map
import ru.er_log.stock.data.network.makeRequest
import ru.er_log.stock.domain.models.`in`.AuthData
import ru.er_log.stock.domain.models.out.SignInRequest
import ru.er_log.stock.domain.repositories.AuthRepository

internal class AuthRepositoryImpl(
    private val authService: AuthService,
    private val authStorage: AuthDataStorage,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : AuthRepository {

    override suspend fun login(request: SignInRequest) = withContext(dispatcher) {
        when (val response = makeRequest { authService.signIn(request.map()) }) {
            is NetworkResult.Failure -> Result.failure(Exception(response.errorMessage, response.t))
            is NetworkResult.Success -> Result.success(response.value.map())
        }
    }

    override suspend fun saveAuthData(authData: AuthData) {
//        TODO("Not yet implemented")
    }

    override suspend fun observeUserLoginState(): Flow<Boolean> = withContext(dispatcher) {
        return@withContext flow {
//            val testUser = UserInfo(
//                "Test User", "todo@user.ru",
//                AuthData("123", 1, emptyList())
//            )
            // TODO
            emit(true)
        }
    }
}