package com.fesskiev.compose.ui.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.createDataStore
import com.fesskiev.compose.ui.utils.Constants.ThemeMode.SYSTEM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class DataStoreManager(context: Context) {

    private val dataStore: DataStore<Preferences> = context.createDataStore(name = "settings")

    companion object {
        private val THEME_MODE = stringPreferencesKey("theme_mode")
    }

    suspend fun setThemeMode(themeMode: String) {
        dataStore.edit { preferences ->
            preferences[THEME_MODE] = themeMode
        }
    }

    fun getThemeMode(): Flow<String> =
        dataStore.data
            .map { preferences ->
                preferences[THEME_MODE] ?: SYSTEM
            }
            .flowOn(Dispatchers.IO)
}