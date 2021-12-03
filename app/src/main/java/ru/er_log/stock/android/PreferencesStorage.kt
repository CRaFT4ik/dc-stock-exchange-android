package ru.er_log.stock.android

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import ru.er_log.stock.data.network.JsonHelpers
import ru.er_log.stock.data.repositories.AuthDataStorage
import ru.er_log.stock.data.repositories.AuthDataStorage.Companion.KEY_AUTH_TOKEN
import ru.er_log.stock.data.repositories.AuthDataStorage.Companion.KEY_USER_PROFILE
import ru.er_log.stock.domain.models.LoggedInUser
import java.io.IOException

object PreferencesStorage : AuthDataStorage {

    @Volatile
    private var prefs: SharedPreferences? = null

    @Synchronized
    fun updateContext(context: Context) {
        prefs = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
    }

    private fun <R> withPrefs(action: (SharedPreferences) -> R): R? {
        when (val prefs = prefs) {
            null -> Log.e(this::class.simpleName, "Attempt to access repository with uninitialized context")
            else -> return action.invoke(prefs)
        }
        return null
    }

    override fun saveAuthToken(token: String) {
        withPrefs {
            val editor = it.edit()
            editor.putString(KEY_AUTH_TOKEN, token)
            editor.apply()
        }
    }

    override fun fetchAuthToken(): String? = withPrefs {
        it.getString(KEY_AUTH_TOKEN, null)
    }

    override fun saveUserProfile(userInfo: LoggedInUser) {
        withPrefs {
            val adapter = JsonHelpers.moshiCleanInstance.adapter(LoggedInUser::class.java)
            val json = adapter.toJson(userInfo)

            val editor = it.edit()
            editor.putString(KEY_USER_PROFILE, json)
            editor.apply()
        }
    }

    override fun fetchUserProfile(): LoggedInUser? = withPrefs {
        val json = it.getString(KEY_USER_PROFILE, null)
        val adapter = JsonHelpers.moshiCleanInstance.adapter(LoggedInUser::class.java)

        if (json != null) try {
            adapter.fromJson(json)
        } catch (e: IOException) {
            null
        } else null
    }
}