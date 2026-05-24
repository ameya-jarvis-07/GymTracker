package com.jarvis.gymtracker.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = ExpressivePrimaryDark,
    onPrimary = ExpressiveOnPrimaryDark,
    background = ExpressiveBackgroundDark,
    surface = ExpressiveSurfaceDark,
    onSurface = ExpressiveOnSurfaceDark,
    primaryContainer = ExpressivePrimary,
    surfaceTint = ExpressiveSurfaceTint
)

private val LightColorScheme = lightColorScheme(
    primary = ExpressivePrimary,
    onPrimary = ExpressiveOnPrimary,
    background = ExpressiveBackground,
    surface = ExpressiveSurface,
    onSurface = ExpressiveOnSurface,
    primaryContainer = ExpressivePrimaryContainer,
    surfaceTint = ExpressiveSurfaceTint
)

@Composable
fun GymTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = androidx.compose.material3.Shapes(),
        content = content
    )
}