package com.fesskiev.compose.ui.utils

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.fesskiev.compose.ui.utils.ThemeMode.DAY
import com.fesskiev.compose.ui.utils.ThemeMode.NIGHT
import com.fesskiev.compose.ui.utils.ThemeMode.SYSTEM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext

fun provideAppSharedPreferences(context: Context): SharedPreferences =
    context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)

object ThemeMode {
    const val DAY = "DAY"
    const val NIGHT = "NIGHT"
    const val SYSTEM = "SYSTEM"
}

class ThemeManager(private val preferences: SharedPreferences) {

    private val THEME_MODE_KEY = "theme_mode"

    suspend fun setThemeMode(themeMode: String) {
        applyNewTheme(themeMode)
        withContext(Dispatchers.Default + NonCancellable) {
            with(preferences.edit()) {
                putString(THEME_MODE_KEY, themeMode)
                commit()
            }
        }
    }

    suspend fun getThemeMode(): String = withContext(Dispatchers.Default) {
        preferences.getString(THEME_MODE_KEY, DAY) ?: DAY
    }

    suspend fun setThemeMode() {
        val theme = getThemeMode()
        applyNewTheme(theme)
    }

    private fun applyNewTheme(themeMode: String) {
        when (themeMode) {
            DAY -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            NIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            SYSTEM -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
                }
            }
        }
    }
}