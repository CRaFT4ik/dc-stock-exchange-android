package ru.er_log.stock.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.er_log.stock.domain.models.`in`.AuthData
import ru.er_log.stock.domain.models.`in`.SignInResponse
import ru.er_log.stock.domain.models.`in`.UserInfo
import ru.er_log.stock.domain.models.out.SignInRequest

interface AuthRepository {
    suspend fun login(request: SignInRequest): Result<SignInResponse>

    suspend fun loginDemo(): Result<SignInResponse>

    suspend fun logout()

    suspend fun getLoggedInUserInfo(): Flow<UserInfo?>

    suspend fun getAuthToken(): String?
}