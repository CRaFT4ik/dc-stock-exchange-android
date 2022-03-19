package ru.er_log.stock.data.storages.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.er_log.stock.data.storages.database.enities.User

@Dao
abstract class UserDao : BaseDao<User>() {
//    @Query("UPDATE User FROM user")
//    fun updateAuthToken(authToken: String, userId: Int)

    @Query("SELECT * FROM user WHERE user.id = :userId")
    abstract fun findUserById(userId: Long): User?

    @Query("SELECT * FROM user WHERE user.username LIKE :username")
    abstract fun findUsersByName(username: String): List<User>

    @Query("SELECT auth_token FROM user WHERE user.id=:userId")
    abstract fun getAuthToken(userId: Long): String?

    @Query("UPDATE user SET auth_token=:authToken WHERE user.id=:userId")
    abstract fun updateAuthToken(userId: Long, authToken: String?)
}