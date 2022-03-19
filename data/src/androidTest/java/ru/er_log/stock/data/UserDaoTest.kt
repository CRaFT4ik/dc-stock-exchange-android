package ru.er_log.stock.android

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.er_log.stock.data.storages.database.StockDatabase
import ru.er_log.stock.data.storages.database.daos.UserDao
import ru.er_log.stock.data.storages.database.enities.User
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class UserDaoTest {
    private lateinit var userDao: UserDao
    private lateinit var db: StockDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, StockDatabase::class.java).build()
        userDao = db.userDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() {
        val user: User = provideUser("test_user")
        userDao.insert(user)
        val byName = userDao.findUsersByName("test_user")
        assertThat(byName[0], equalTo(user))
    }

    @Test
    fun getAuthTokenTest() {
        val user: User = provideUser("test_user")
        userDao.insert(user)

        val token = userDao.getAuthToken(user.id)
        assertThat(byName[0], equalTo(user))
    }

    private fun provideUser(name: String): User {
        return User(1, name, "email", "123")
    }
}