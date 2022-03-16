package ru.er_log.stock.domain.usecases

import ru.er_log.stock.domain.models.`in`.Transaction
import ru.er_log.stock.domain.models.`in`.UserCard
import ru.er_log.stock.domain.repositories.AccountRepository

class AccountUseCases(private val accountRepository: AccountRepository) {

    val fetchOperations: UseCase<Int, List<Transaction>> by lazy { FetchOperationsUseCase() }
    val fetchUserCard: UseCase<Unit, UserCard> by lazy { FetchUserCardUseCase() }

    private inner class FetchOperationsUseCase : UseCaseValue<Int, List<Transaction>>() {
        override suspend fun run(pageIndex: Int): Result<List<Transaction>> {
            val pageSize = 50
            return accountRepository.fetchTransactions(pageSize, offset = pageIndex * pageSize)
        }
    }

    private inner class FetchUserCardUseCase : UseCaseValue<Unit, UserCard>() {
        override suspend fun run(params: Unit): Result<UserCard> {
            return accountRepository.fetchUserCard()
        }
    }
}