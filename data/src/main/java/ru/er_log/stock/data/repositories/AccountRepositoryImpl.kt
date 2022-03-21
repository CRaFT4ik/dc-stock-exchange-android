package ru.er_log.stock.data.repositories

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import ru.er_log.stock.data.network.NetworkResult
import ru.er_log.stock.data.network.api.v1.account.AccountService
import ru.er_log.stock.data.network.api.v1.account.map
import ru.er_log.stock.data.network.makeRequest
import ru.er_log.stock.data.storages.database.daos.UserDao
import ru.er_log.stock.domain.models.`in`.Transaction
import ru.er_log.stock.domain.repositories.AccountRepository

internal class AccountRepositoryImpl(
    private val accountService: AccountService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : AccountRepository {

    // TODO: add local cache
    override suspend fun getTransactions(limit: Int, offset: Int) = withContext(dispatcher) {
        when (val response = makeRequest { accountService.getTransactions(limit, offset) }) {
            is NetworkResult.Failure -> Result.failure(Exception(response.errorMessage, response.t))
            is NetworkResult.Success -> Result.success(response.value.map())
        }
    }

    override suspend fun getUserCard() = withContext(dispatcher) {
        when (val response = makeRequest { accountService.getProfile() }) {
            is NetworkResult.Failure -> Result.failure(Exception(response.errorMessage, response.t))
            is NetworkResult.Success -> Result.success(response.value.map())
        }
    }
}