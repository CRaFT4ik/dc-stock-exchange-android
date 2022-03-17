package ru.er_log.stock.data.storages.database.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.er_log.stock.data.storages.database.enities.User

@Dao
interface UserDao {// : BaseDao<User> {
//    @Query("UPDATE User FROM user")
//    fun updateAuthToken(authToken: String, userId: Int)

    @Update
    fun update(obj: User)



    @Query("SELECT auth_token FROM user WHERE user.id=:userId")
    fun getAuthToken(userId: Long): String?

    @Query("UPDATE user SET auth_token=:authToken WHERE user.id=:userId")
    fun updateAuthToken(userId: Long, authToken: String?)
}