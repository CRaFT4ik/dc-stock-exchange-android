package ru.er_log.stock.domain.api.v1.requests

import com.squareup.moshi.Json
import java.math.BigDecimal

data class LotCreationRequest(
    @Json(name = "price")
    val price: BigDecimal
)
