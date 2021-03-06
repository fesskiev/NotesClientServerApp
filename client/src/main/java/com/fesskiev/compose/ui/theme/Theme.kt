package com.fesskiev.compose.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColors(
    primary = BlueLight700,
    primaryVariant = BlueLight900,
    onPrimary = Color.White,
    secondary = BlueLight700,
    secondaryVariant = BlueLight900,
    onSecondary = Color.White,
    error = Red800,
    background = Color.White,
    surface = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White
)

private val DarkColors = darkColors(
    primary = BlueLight300,
    primaryVariant = BlueLight700,
    onPrimary = Color.Black,
    secondary = BlueLight300,
    onSecondary = Color.White,
    error = Red300,
    background = Color.Black,
    surface = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Color.Black
)

val Grey = Color(0xFFCACACA)

@Composable
fun AppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(
        colors = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
