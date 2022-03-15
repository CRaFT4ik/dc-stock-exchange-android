package ru.er_log.stock.data.network.api.v1.account

import com.squareup.moshi.FromJson
import com.squareup.moshi.Json
import com.squareup.moshi.ToJson
import ru.er_log.stock.data.network.api.Mappable
import ru.er_log.stock.domain.models.`in`.Lot
import ru.er_log.stock.domain.models.`in`.Transaction
import java.math.BigDecimal

internal data class TransactionDto(
    @Json(name = "id")
    val uid: String,
    @Json(name = "price")
    val price: BigDecimal,
    @Json(name = "amount")
    val amount: BigDecimal,
    @Json(name = "timestamp")
    val timestamp: Long,
    @Json(name = "type")
    val type: Type,
    @Json(name = "is_active")
    val isPending: Boolean
) : Mappable<Transaction> {

    override fun map() = Transaction(
        uid = uid,
        lot = Lot(price, amount),
        timestamp = timestamp,
        type = type.map(),
        isPending = isPending
    )

    internal enum class Type : Mappable<Transaction.Type> {
        UNKNOWN,
        OFFER,
        ORDER;

        override fun map(): Transaction.Type {
            return when (this) {
                UNKNOWN -> Transaction.Type.UNKNOWN
                OFFER -> Transaction.Type.OFFER
                ORDER -> Transaction.Type.ORDER
            }
        }

        class JsonAdapter {
            @ToJson fun toJson(obj: Type): String = obj.name
            @FromJson fun fromJson(value: String): Type =
                values().firstOrNull { it.name.equals(value, ignoreCase = true) } ?: UNKNOWN
        }
    }
}