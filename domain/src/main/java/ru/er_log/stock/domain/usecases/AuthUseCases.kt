package ru.er_log.stock.domain.usecases

import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import ru.er_log.stock.data.network.api.v1.auth.SignInRequestDto
import ru.er_log.stock.domain.models.`in`.UserInfo
import ru.er_log.stock.domain.repositories.AuthRepository

class AuthUseCases(private val authRepository: AuthRepository) {

    val signUp: UseCase<Unit, Unit> by lazy { SignUpUseCase() }
    val signIn: UseCase<SignInRequestDto, UserInfo> by lazy { SignInUseCase() }
    val observeUser: UseCase<Unit, UserInfo?> by lazy { ObserveUserProfile() }

    private inner class SignUpUseCase : UseCaseValue<Unit, Unit>() {
        override suspend fun run(params: Unit): Result<Unit> {
            TODO("Not yet implemented")
            // repository.register(request)
        }
    }

    private inner class SignInUseCase : UseCaseValue<SignInRequestDto, UserInfo>() {
        override suspend fun run(params: SignInRequestDto): Result<UserInfo> {
            // TODO: auth repository - save token & user data
            return authRepository.login(params)
        }
    }

    private inner class ObserveUserProfile : UseCaseFlow<Unit, UserInfo?>() {
        override suspend fun run(params: Unit, onEach: suspend (Result<UserInfo?>) -> Unit) {
            authRepository.observeUserProfile()
                .catch { onEach(Result.failure(it)) }
                .collect { onEach(Result.success(it)) }
        }
    }
}