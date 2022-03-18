package ru.er_log.stock.data.repositories

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.er_log.stock.data.network.NetworkResult
import ru.er_log.stock.data.network.api.v1.auth.AuthService
import ru.er_log.stock.data.network.api.v1.auth.map
import ru.er_log.stock.data.network.makeRequest
import ru.er_log.stock.data.storages.AuthDataStore
import ru.er_log.stock.data.storages.database.daos.UserDao
import ru.er_log.stock.data.storages.database.enities.User
import ru.er_log.stock.domain.models.`in`.UserInfo
import ru.er_log.stock.domain.models.out.SignInRequest
import ru.er_log.stock.domain.repositories.AuthRepository

internal class AuthRepositoryImpl(
    private val userDao: UserDao,
    private val authService: AuthService,
    private val authStore: AuthDataStore,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : AuthRepository {

    override suspend fun login(request: SignInRequest) = withContext(dispatcher) {
        when (val response = makeRequest { authService.signIn(request.map()) }) {
            is NetworkResult.Failure -> Result.failure(Exception(response.errorMessage, response.t))
            is NetworkResult.Success -> {
                val signInData = response.value
                userDao.upsert(
                    User(
                        id = signInData.userId,
                        username = signInData.userName,
                        email = signInData.userEmail,
                        authToken = signInData.token
                    )
                )
                authStore.saveLoggedInUserId(signInData.userId)
                Result.success(response.value.map())
            }
        }
    }

    override suspend fun logout() {
        withContext(dispatcher) {
            authStore.getLoggedInUserId().firstOrNull()?.let { userId ->
                userDao.updateAuthToken(userId, null)
                authStore.saveLoggedInUserId(null)
            }
        }
    }

    override suspend fun getLoggedInUserInfo(): Flow<UserInfo?> = withContext(dispatcher) {
        authStore.getLoggedInUserId().distinctUntilChanged().map {
            it?.let { userId ->
                userDao.findUserById(userId)?.let { user ->
                    UserInfo(user.username, user.email)
                }
            }
        }
    }

    override suspend fun getAuthToken(): String? = withContext(dispatcher) {
        authStore.getLoggedInUserId().firstOrNull()?.let { userId ->
            userDao.getAuthToken(userId)
        }
    }
}