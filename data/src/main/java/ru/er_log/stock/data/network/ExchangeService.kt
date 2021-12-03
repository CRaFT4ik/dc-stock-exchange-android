package ru.er_log.stock.data.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import ru.er_log.stock.domain.boundaries.requests.LotCreationRequest
import ru.er_log.stock.domain.boundaries.responses.ActiveLotsResponse
import ru.er_log.stock.domain.boundaries.responses.DealsResponse

internal interface ExchangeService {

    @GET("exchange/lots")
    suspend fun fetchActiveLots(): ActiveLotsResponse

    @GET("exchange/deals")
    suspend fun fetchDeals(): DealsResponse

    @POST("exchange/buy")
    suspend fun createPurchaseLot(@Body request: LotCreationRequest)

    @POST("exchange/sell")
    suspend fun createSaleLot(@Body request: LotCreationRequest)
}
