package ru.er_log.stock.data.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.er_log.stock.data.network.AuthService
import ru.er_log.stock.data.network.NetworkResult
import ru.er_log.stock.data.network.makeRequest
import ru.er_log.stock.domain.boundaries.SignInRequest
import ru.er_log.stock.domain.models.LoggedInUser
import ru.er_log.stock.domain.repositories.AuthRepository

internal class AuthRepositoryImpl(
    private val api: AuthService
) : AuthRepository {

    override fun login(request: SignInRequest): Flow<LoggedInUser> = flow {
        when (val response = makeRequest { api.signIn(request) }) {
            is NetworkResult.Failure -> throw Exception(response.errorMessage, response.t)
            is NetworkResult.Success -> emit(response.value.map())
        }
    }
}