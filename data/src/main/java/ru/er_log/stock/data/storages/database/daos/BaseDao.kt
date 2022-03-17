package ru.er_log.stock.data.storages.database.daos

import androidx.room.*

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(obj: T): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(obj: List<T>): List<Long>

    @Update
    fun update(obj: T)

    @Update
    fun update(obj: List<T>)

    @Delete
    fun delete(obj: T)

    @Transaction
    fun upsert(obj: T) {
        if (insert(obj) == -1L) {
            update(obj)
        }
    }

    @Transaction
    fun upsert(objList: List<T>) {
        val insertResult = insert(objList)
        val updateList: MutableList<T> = ArrayList()
        for (i in insertResult.indices) {
            if (insertResult[i] == -1L) {
                updateList.add(objList[i])
            }
        }
        if (updateList.isNotEmpty()) {
            update(updateList)
        }
    }
}