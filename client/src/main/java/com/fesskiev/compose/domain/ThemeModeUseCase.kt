package com.fesskiev.compose.domain

import com.fesskiev.compose.ui.utils.ThemeManager

class ThemeModeUseCase(private val themeManager: ThemeManager) {

    suspend fun setThemeMode(themeMode: String) {
        themeManager.setThemeMode(themeMode)
    }

    suspend fun getThemeMode(): String = themeManager.getThemeMode()
}