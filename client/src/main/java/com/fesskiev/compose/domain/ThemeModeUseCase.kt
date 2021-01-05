package com.fesskiev.compose.domain

import com.fesskiev.compose.ui.utils.DataStoreManager
import com.fesskiev.compose.ui.utils.ThemeManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ThemeModeUseCase(
    private val dataStoreManager: DataStoreManager,
    private val themeManager: ThemeManager
) {

    fun setThemeMode(themeMode: String): Flow<Unit> = flow {
        dataStoreManager.setThemeMode(themeMode)
        themeManager.applyNewTheme(themeMode)
    }

    fun getThemeMode(): Flow<String> = dataStoreManager.getThemeMode()
}