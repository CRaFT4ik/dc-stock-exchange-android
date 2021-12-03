package ru.er_log.stock.data.network

import retrofit2.http.GET
import ru.er_log.stock.domain.boundaries.responses.ActiveLotsResponse
import ru.er_log.stock.domain.boundaries.responses.DealsResponse

internal interface ExchangeService {

    @GET("exchange/lots")
    suspend fun fetchActiveLots(): ActiveLotsResponse

    @GET("exchange/deals")
    suspend fun fetchDeals(): DealsResponse
}
