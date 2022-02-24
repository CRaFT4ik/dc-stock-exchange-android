package ru.er_log.stock.domain.repositories

import ru.er_log.stock.domain.boundaries.requests.SignInRequest
import ru.er_log.stock.domain.models.LoggedInUser

interface AuthRepository {
    suspend fun login(request: SignInRequest): Result<LoggedInUser>
}