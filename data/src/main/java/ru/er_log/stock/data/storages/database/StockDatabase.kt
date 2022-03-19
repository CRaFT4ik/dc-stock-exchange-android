package ru.er_log.stock.data.storages.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.er_log.stock.data.storages.database.daos.UserDao
import ru.er_log.stock.data.storages.database.enities.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class StockDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}