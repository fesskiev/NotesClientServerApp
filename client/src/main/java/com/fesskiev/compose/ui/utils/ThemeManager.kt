package com.fesskiev.compose.ui.utils

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.fesskiev.compose.ui.utils.Constants.ThemeMode.DAY
import com.fesskiev.compose.ui.utils.Constants.ThemeMode.NIGHT
import com.fesskiev.compose.ui.utils.Constants.ThemeMode.SYSTEM

class ThemeManager {

    fun applyNewTheme(themeMode: String) {
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