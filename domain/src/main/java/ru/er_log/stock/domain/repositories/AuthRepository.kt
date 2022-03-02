package ru.er_log.stock.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.er_log.stock.domain.api.v1.auth.SignInRequest
import ru.er_log.stock.domain.models.auth.UserProfile

interface AuthRepository {
    suspend fun login(request: SignInRequest): Result<UserProfile>

    suspend fun fetchUserProfile(): Flow<UserProfile?>
}