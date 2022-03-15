package ru.er_log.stock.data.network.api.v1.exchange

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ru.er_log.stock.domain.models.`in`.Lot

internal interface ExchangeService {

    @GET("exchange/order_book")
    suspend fun fetchOrdersBook(
        @Query("limit") limit: Int
    ): OrderBookDto

    @POST("exchange/order")
    suspend fun createOrder(@Body request: Lot)

    @POST("exchange/offer")
    suspend fun createOffer(@Body request: Lot)
}
