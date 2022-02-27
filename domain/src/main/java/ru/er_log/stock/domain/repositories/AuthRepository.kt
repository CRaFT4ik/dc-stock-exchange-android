package ru.er_log.stock.domain.repositories

import ru.er_log.stock.domain.api.v1.auth.SignInRequest
import ru.er_log.stock.domain.models.auth.LoggedInUser

interface AuthRepository {
    suspend fun login(request: SignInRequest): Result<LoggedInUser>
}