package ru.er_log.stock.domain.repositories

import ru.er_log.stock.domain.models.`in`.Transaction
import ru.er_log.stock.domain.models.`in`.UserCard

interface AccountRepository {

    suspend fun fetchTransactions(limit: Int, offset: Int): Result<List<Transaction>>

    suspend fun fetchUserCard(): Result<UserCard>
}