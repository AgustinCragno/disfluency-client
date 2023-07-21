package com.disfluency.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat


private val LightColorScheme = lightColorScheme(
    primary = Orange50,
    onPrimary = Color.White,
    primaryContainer = Orange20,
    onPrimaryContainer = Orange90,

    secondary = Blue50,
    onSecondary = Color.White,
    secondaryContainer = Blue70,
    onSecondaryContainer = Blue20,

    surfaceVariant = Orange99,

    background = Color.White,
    onBackground = Blue10,
    surface = Orange99,
    onSurface = Grey10,
    onSurfaceVariant = Orange40,

    error = Red40,
    onError = Color.White,
    outline = Orange60,

    surfaceTint = Blue99
)

@Composable
fun DisfluencyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = LightColorScheme.primary.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}