package ru.er_log.stock.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.er_log.stock.domain.models.`in`.UserInfo
import ru.er_log.stock.domain.models.out.SignInData

interface AuthRepository {
    suspend fun login(request: SignInData): Result<UserInfo>

    suspend fun observeUserProfile(): Flow<UserInfo?>
}