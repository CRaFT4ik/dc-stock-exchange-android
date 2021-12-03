package ru.er_log.stock.domain.usecases

import ru.er_log.stock.domain.boundaries.requests.SignInRequest
import ru.er_log.stock.domain.repositories.AuthRepository

class AuthUseCase(private val repository: AuthRepository) {
    val signIn = SignInUseCase()
    val signUp = SignUpUseCase()

    inner class SignInUseCase {
        operator fun invoke(request: SignInRequest) = repository.login(request)
    }

    inner class SignUpUseCase {
//        operator fun invoke(request: SignUpRequest) = repository.register(request)
    }
}