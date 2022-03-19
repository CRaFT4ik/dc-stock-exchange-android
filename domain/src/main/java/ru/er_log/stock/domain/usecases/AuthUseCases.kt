package ru.er_log.stock.domain.usecases

import kotlinx.coroutines.flow.catch
import ru.er_log.stock.domain.models.`in`.UserInfo
import ru.er_log.stock.domain.models.out.SignInRequest
import ru.er_log.stock.domain.repositories.AuthRepository

class AuthUseCases(private val authRepository: AuthRepository) {

    val signUp: UseCase<Unit, Unit> by lazy { SignUpUseCase() }
    val signIn: UseCase<SignInRequest, UserInfo> by lazy { SignInUseCase() }
    val signOut: UseCase<Unit, Unit> by lazy { SignOutUseCase() }
    val observeLoginState: UseCase<Unit, UserInfo?> by lazy { ObserveLoggedInUser() }

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

    private inner class SignOutUseCase : UseCaseValue<Unit, Unit>() {
        override suspend fun run(params: Unit): Result<Unit> {
            return Result.success(authRepository.logout())
        }
    }

    private inner class ObserveLoggedInUser : UseCaseFlow<Unit, UserInfo?>() {
        override suspend fun run(params: Unit, onEach: suspend (Result<UserInfo?>) -> Unit) {
            authRepository.getLoggedInUserInfo()
                .catch { onEach(Result.failure(it)) }
                .collect { onEach(Result.success(it)) }
        }
    }
}