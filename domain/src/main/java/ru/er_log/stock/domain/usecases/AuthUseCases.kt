package ru.er_log.stock.domain.usecases

import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import ru.er_log.stock.domain.api.v1.auth.SignInRequest
import ru.er_log.stock.domain.models.auth.UserProfile
import ru.er_log.stock.domain.repositories.AuthRepository

class AuthUseCases(private val authRepository: AuthRepository) {

    val signUp: UseCase<Unit, Unit> by lazy { SignUpUseCase() }
    val signIn: UseCase<SignInRequest, UserProfile> by lazy { SignInUseCase() }
    val fetchUser: UseCase<Unit, UserProfile?> by lazy { FetchUserProfile() }

    private inner class SignUpUseCase : UseCaseValue<Unit, Unit>() {
        override suspend fun run(params: Unit): Result<Unit> {
            TODO("Not yet implemented")
            // repository.register(request)
        }
    }

    private inner class SignInUseCase : UseCaseValue<SignInRequest, UserProfile>() {
        override suspend fun run(params: SignInRequest): Result<UserProfile> {
            // TODO: auth repository - save token & user data
            return authRepository.login(params)
        }
    }

    private inner class FetchUserProfile : UseCaseFlow<Unit, UserProfile?>() {
        override suspend fun run(params: Unit, onEach: suspend (Result<UserProfile?>) -> Unit) {
            authRepository.fetchUserProfile()
                .catch { onEach(Result.failure(it)) }
                .collect { onEach(Result.success(it)) }
        }
    }
}