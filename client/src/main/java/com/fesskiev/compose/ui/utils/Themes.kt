package com.fesskiev.compose.ui.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val lightThemeColors = lightColors(
        primary = Color(0xFF073AD5),
        primaryVariant = Color.White,
        onPrimary = Color.White,
        secondary = Color.White,
        onSecondary = Color.Black,
        background = Color(0xFFE7E7E7),
        onBackground = Color.Black,
        surface = Color.White,
        onSurface = Color.Black,
        error = Color.Red,
        onError = Color.White
)

val darkThemeColors = darkColors(
        primary = Color(0xFF01124D),
        primaryVariant = Color(0xFF5080C4),
        onPrimary = Color.Black,
        secondary = Color(0xFF121212),
        onSecondary = Color.White,
        surface = Color(0xFF121212),
        background = Color(0xFF121212),
        onBackground = Color.White,
        onSurface = Color.White
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val dark = isSystemInDarkTheme()
    val colorPalette = if (dark) {
        darkThemeColors
    } else {
        lightThemeColors
    }
    MaterialTheme(colors = colorPalette) { content() }
}
