package ru.er_log.stock.data.network.api.v1.account

import retrofit2.http.GET
import retrofit2.http.Query

internal interface AccountService {

    @GET("account/transactions")
    suspend fun fetchTransactions(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): List<TransactionDto>

    @GET("account/profile")
    suspend fun fetchProfile(): UserCardDto
}
