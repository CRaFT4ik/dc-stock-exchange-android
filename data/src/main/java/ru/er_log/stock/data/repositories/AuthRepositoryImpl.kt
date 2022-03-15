package ru.er_log.stock.data.repositories

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import ru.er_log.stock.data.network.api.v1.auth.AuthService
import ru.er_log.stock.data.network.NetworkResult
import ru.er_log.stock.data.network.makeRequest
import ru.er_log.stock.data.network.api.v1.auth.SignInRequestDto
import ru.er_log.stock.domain.models.`in`.AuthData
import ru.er_log.stock.domain.models.`in`.UserInfo
import ru.er_log.stock.domain.repositories.AuthRepository

internal class AuthRepositoryImpl(
    private val authService: AuthService,
    private val authStorage: AuthDataStorage,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : AuthRepository {

    override suspend fun login(request: SignInRequestDto) = withContext(dispatcher) {
        when (val response = makeRequest { authService.signIn(request) }) {
            is NetworkResult.Failure -> Result.failure(Exception(response.errorMessage, response.t))
            is NetworkResult.Success -> Result.success(response.value.map())
        }
    }

    override suspend fun observeUserProfile(): Flow<UserInfo?> = withContext(dispatcher) {
        return@withContext flow {
            val testUser = UserInfo(
                "Test User", "todo@user.ru",
                AuthData("123", 1, emptyList())
            )

            emit(testUser)
        }
    }
}