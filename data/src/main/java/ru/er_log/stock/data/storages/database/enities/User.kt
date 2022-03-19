package ru.er_log.stock.data.storages.database.enities

import androidx.room.*

@Entity
data class User(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "auth_token") val authToken: String?
//    @Relation(parentColumn = "id", entityColumn = "id")
//    val operations: List<LotOperation>
)