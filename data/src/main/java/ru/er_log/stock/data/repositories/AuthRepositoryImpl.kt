package ru.er_log.stock.data.repositories

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import ru.er_log.stock.data.network.AuthService
import ru.er_log.stock.data.network.NetworkResult
import ru.er_log.stock.data.network.makeRequest
import ru.er_log.stock.domain.api.v1.auth.SignInRequest
import ru.er_log.stock.domain.models.auth.UserProfile
import ru.er_log.stock.domain.repositories.AuthRepository
import kotlin.random.Random

internal class AuthRepositoryImpl(
    private val authService: AuthService,
    private val authStorage: AuthDataStorage,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : AuthRepository {

    override suspend fun login(request: SignInRequest) = withContext(dispatcher) {
        when (val response = makeRequest { authService.signIn(request) }) {
            is NetworkResult.Failure -> Result.failure(Exception(response.errorMessage, response.t))
            is NetworkResult.Success -> Result.success(response.value.map())
        }
    }

    override suspend fun observeUserProfile(): Flow<UserProfile?> = withContext(dispatcher) {
        return@withContext flow {
            val testUser = UserProfile(
                "TEST USER", "todo@user.ru",
                UserProfile.AuthData("123", 1, emptyList())
            )

            delay(1000)
            if (Random.nextBoolean()) {
                emit(testUser)
            } else {
                emit(null)
            }
        }
    }
}