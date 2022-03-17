package ru.er_log.stock.domain.usecases

import kotlinx.coroutines.flow.catch
import ru.er_log.stock.domain.models.`in`.UserInfo
import ru.er_log.stock.domain.models.out.SignInRequest
import ru.er_log.stock.domain.repositories.AuthRepository

class AuthUseCases(private val authRepository: AuthRepository) {

    val signUp: UseCase<Unit, Unit> by lazy { SignUpUseCase() }
    val signIn: UseCase<SignInRequest, UserInfo> by lazy { SignInUseCase() }
    val observeLoginState: UseCase<Unit, Boolean> by lazy { ObserveUserLoginState() }

    private inner class SignUpUseCase : UseCaseValue<Unit, Unit>() {
        override suspend fun run(params: Unit): Result<Unit> {
            TODO("Not yet implemented")
        }
    }

    private inner class SignInUseCase : UseCaseValue<SignInRequest, UserInfo>() {
        override suspend fun run(params: SignInRequest): Result<UserInfo> {
            val response = authRepository.login(params)
                .getOrElse { return Result.failure(it) }
            return Result.success(response.userInfo)
        }
    }

    private inner class ObserveUserLoginState : UseCaseFlow<Unit, Boolean>() {
        override suspend fun run(params: Unit, onEach: suspend (Result<Boolean>) -> Unit) {
            authRepository.observeUserLoginState()
                .catch { onEach(Result.failure(it)) }
                .collect { onEach(Result.success(it)) }
        }
    }
}