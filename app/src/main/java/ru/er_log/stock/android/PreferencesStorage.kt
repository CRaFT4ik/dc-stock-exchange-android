package ru.er_log.stock.android

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import ru.er_log.stock.data.repositories.AuthTokenStorage
import ru.er_log.stock.data.repositories.AuthTokenStorage.Companion.KEY_AUTH_TOKEN

object PreferencesStorage : AuthTokenStorage {

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
}