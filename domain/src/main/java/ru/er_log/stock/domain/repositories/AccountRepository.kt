package ru.er_log.stock.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.er_log.stock.domain.models.`in`.Transaction
import ru.er_log.stock.domain.models.`in`.UserCard

interface AccountRepository {

    suspend fun getTransactions(limit: Int, offset: Int): Result<List<Transaction>>

    suspend fun getUserCard(): Result<UserCard>
}