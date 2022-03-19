package ru.er_log.stock.data.storages

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

internal class AuthDataStore(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val LAST_LOGGED_IN_USER_ID = longPreferencesKey("LAST_LOGGED_IN_USER_ID")
    }

    suspend fun saveLoggedInUserId(userId: Long?) {
        dataStore.edit {
            it[LAST_LOGGED_IN_USER_ID] = userId ?: -1L
        }
    }

    fun getLoggedInUserId(): Flow<Long?> {
        return dataStore.data.map {
            it[LAST_LOGGED_IN_USER_ID]?.let { v -> if (v == -1L) null else v }
        }
    }
}
