package ru.er_log.stock.data.storages.database.enities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.math.BigDecimal
import java.util.*

@Entity
data class LotOperation(
    @PrimaryKey val id: UUID,
    @ColumnInfo(name = "owner") val owner: User,
    @ColumnInfo(name = "price") val price: BigDecimal,
    @ColumnInfo(name = "amount") val amount: BigDecimal,
    @ColumnInfo(name = "timestamp") val timestamp: Long,
)