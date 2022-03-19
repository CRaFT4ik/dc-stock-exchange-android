package ru.er_log.stock.android.base.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPrefsStore(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val IS_LIGHT_THEME = booleanPreferencesKey("IS_LIGHT_THEME")
    }

    suspend fun setIsLightTheme(isLightTheme: Boolean) {
        dataStore.edit {
            it[IS_LIGHT_THEME] = isLightTheme
        }
    }

    fun getIsLightTheme(): Flow<Boolean?> {
        return dataStore.data.map { it[IS_LIGHT_THEME] }
    }
}
