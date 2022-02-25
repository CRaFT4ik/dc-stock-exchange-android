package ru.er_log.stock.domain.repositories

import ru.er_log.stock.domain.api.v1.requests.SignInRequest
import ru.er_log.stock.domain.models.LoggedInUser

interface AuthRepository {
    suspend fun login(request: SignInRequest): Result<LoggedInUser>
}