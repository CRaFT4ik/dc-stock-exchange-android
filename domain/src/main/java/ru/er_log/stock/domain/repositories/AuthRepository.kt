package ru.er_log.stock.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.er_log.stock.domain.boundaries.SignInRequest
import ru.er_log.stock.domain.models.LoggedInUser

interface AuthRepository {
    fun login(request: SignInRequest): Flow<LoggedInUser>
}