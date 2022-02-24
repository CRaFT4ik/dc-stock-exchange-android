package ru.er_log.stock.domain.usecases

import ru.er_log.stock.domain.boundaries.requests.SignInRequest
import ru.er_log.stock.domain.models.LoggedInUser
import ru.er_log.stock.domain.repositories.AuthRepository

class AuthUseCase(private val authRepository: AuthRepository) {
    val signIn = SignInUseCase()
    val signUp = SignUpUseCase()

    inner class SignInUseCase : UseCase<SignInRequest, LoggedInUser>() {
        override suspend fun run(params: SignInRequest): Result<LoggedInUser> {
            return authRepository.login(params)
        }
    }

    inner class SignUpUseCase {
//        operator fun invoke(request: SignUpRequest) = repository.register(request)
    }
}