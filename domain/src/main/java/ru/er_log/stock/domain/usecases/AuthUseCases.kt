package ru.er_log.stock.domain.usecases

import ru.er_log.stock.domain.api.v1.requests.SignInRequest
import ru.er_log.stock.domain.models.LoggedInUser
import ru.er_log.stock.domain.repositories.AuthRepository

class AuthUseCases(private val authRepository: AuthRepository) {

    val signIn: UseCase<SignInRequest, LoggedInUser> by lazy { SignInUseCase() }
    val signUp: UseCase<Unit, Unit> by lazy { SignUpUseCase() }

    private inner class SignInUseCase : UseCaseValue<SignInRequest, LoggedInUser>() {
        override suspend fun run(params: SignInRequest): Result<LoggedInUser> {
            return authRepository.login(params)
        }
    }

    private inner class SignUpUseCase : UseCaseValue<Unit, Unit>() {
        override suspend fun run(params: Unit): Result<Unit> {
            TODO("Not yet implemented")
            // repository.register(request)
        }
    }
}