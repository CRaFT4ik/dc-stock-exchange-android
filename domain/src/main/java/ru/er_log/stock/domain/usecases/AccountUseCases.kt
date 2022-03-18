package ru.er_log.stock.domain.usecases

import ru.er_log.stock.domain.models.`in`.Transaction
import ru.er_log.stock.domain.models.`in`.UserCard
import ru.er_log.stock.domain.repositories.AccountRepository

class AccountUseCases(private val accountRepository: AccountRepository) {

    val getOperations: UseCase<Int, List<Transaction>> by lazy { GetOperationsUseCase() }
    val getUserCard: UseCase<Unit, UserCard> by lazy { GetUserCardUseCase() }

    private inner class GetOperationsUseCase : UseCaseValue<Int, List<Transaction>>() {
        override suspend fun run(pageIndex: Int): Result<List<Transaction>> {
            val pageSize = 50
            return accountRepository.getTransactions(pageSize, offset = pageIndex * pageSize)
        }
    }

    private inner class GetUserCardUseCase : UseCaseValue<Unit, UserCard>() {
        override suspend fun run(params: Unit): Result<UserCard> {
            return accountRepository.getUserCard()
        }
    }
}