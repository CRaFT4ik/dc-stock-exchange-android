package ru.er_log.stock.data.storages

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.*

internal class AuthDataStore(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val LAST_LOGGED_IN_USER_ID = longPreferencesKey("LAST_LOGGED_IN_USER_ID")
    }

    suspend fun saveLoggedInUserId(userId: Long) {
        dataStore.edit {
            it[LAST_LOGGED_IN_USER_ID] = userId
        }
    }

    fun fetchLoggedInUserIdFlow(): Flow<Long?> {
        return dataStore.data.map { it[LAST_LOGGED_IN_USER_ID] }
    }

    suspend fun fetchLoggedInUserId(): Long? {
        return fetchLoggedInUserIdFlow().lastOrNull()
    }
}

private suspend fun <T> Flow<T>.lastOrNull(): T? {
    var result: T? = null
    catch { return@catch }.collectLatest { result = it; return@collectLatest }
    return result
}