package ru.er_log.stock.domain.usecases

import ru.er_log.stock.domain.models.`in`.Transaction
import ru.er_log.stock.domain.models.`in`.UserCard
import ru.er_log.stock.domain.repositories.AccountRepository

class AccountUseCases(private val accountRepository: AccountRepository) {

    val getOperations: UseCase<Int, List<Transaction>> by lazy { ObserveOperationsUseCase() }
    val getUserCard: UseCase<Unit, UserCard> by lazy { ObserveUserCardUseCase() }

    private inner class ObserveOperationsUseCase : UseCaseRepeatable<Int, List<Transaction>>(
        delayMs = 3000
    ) {
        override suspend fun run(pageIndex: Int): Result<List<Transaction>> {
            val pageSize = 50
            return accountRepository.getTransactions(pageSize, offset = pageIndex * pageSize)
        }
    }

    private inner class ObserveUserCardUseCase : UseCaseRepeatable<Unit, UserCard>(
        delayMs = 10000
    ) {
        override suspend fun run(params: Unit): Result<UserCard> {
            return accountRepository.getUserCard()
        }
    }
}