// File: /Users/williampark/AndroidStudioProjects/DirtyStream/attackapp/src/main/java/com/artexplorer/museum/ui/theme/Theme.kt
package com.artexplorer.museum.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Define your app's color palette for Compose
private val DarkColorScheme = darkColorScheme(
    primary = museum_primary,
    secondary = museum_secondary,
    tertiary = museum_primary_variant,
    background = museum_background_dark,
    surface = museum_surface_dark,
    onPrimary = museum_on_primary_dark,
    onSecondary = museum_on_secondary_dark,
    onBackground = museum_on_background_dark,
    onSurface = museum_on_surface_dark
)

private val LightColorScheme = lightColorScheme(
    primary = museum_primary,
    secondary = museum_secondary,
    tertiary = museum_primary_variant,
    background = museum_background,  // Using XML background
    surface = museum_surface,        // Using XML surface
    onPrimary = museum_on_primary_light,
    onSecondary = museum_on_secondary_light,
    onBackground = museum_on_background_light,
    onSurface = museum_on_surface_light
)

@Composable
fun MuseumTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}