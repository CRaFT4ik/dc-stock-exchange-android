package ru.er_log.stock.android.base.storages

import android.content.Context
import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import ru.er_log.stock.android.R
import ru.er_log.stock.data.network.JsonHelpers
import ru.er_log.stock.data.repositories.AuthDataStorage
import ru.er_log.stock.data.repositories.AuthDataStorage.Companion.KEY_AUTH_TOKEN
import ru.er_log.stock.data.repositories.AuthDataStorage.Companion.KEY_USER_PROFILE
import ru.er_log.stock.domain.models.auth.UserProfile
import java.io.IOException

class PreferencesStorage(
    context: Context,
    private val moshi: Moshi
) : AuthDataStorage {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    override fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(KEY_AUTH_TOKEN, token)
        editor.apply()
    }

    override fun fetchAuthToken(): String? {
        return prefs.getString(KEY_AUTH_TOKEN, null)
    }

    override fun saveUserProfile(userInfo: UserProfile) {
        prefs.apply {
            val adapter = moshi.adapter(UserProfile::class.java)
            val json = adapter.toJson(userInfo)

            val editor = edit()
            editor.putString(KEY_USER_PROFILE, json)
            editor.apply()
        }
    }

    override fun fetchUserProfile(): UserProfile? = prefs.run {
        val json = getString(KEY_USER_PROFILE, null)
        val adapter = moshi.adapter(UserProfile::class.java)

        if (json != null) try {
            adapter.fromJson(json)
        } catch (e: IOException) {
            null
        } else null
    }
}