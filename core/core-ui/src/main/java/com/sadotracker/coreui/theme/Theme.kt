package com.sadotracker.coreui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Sado Tracker is permanently dark themed (Shadcn aesthetic constraint from PRD)
private val ShadcnDarkModeColors = darkColorScheme(
    primary = PrimaryAccent,
    secondary = Zinc400,
    tertiary = Zinc600,
    background = Background,
    surface = SurfaceColor,
    onPrimary = Zinc900,
    onSecondary = TextPrimary,
    onTertiary = TextPrimary,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    outline = BorderColor,
    error = ErrorColor
)

@Composable
fun SadoTrackerTheme(
    // Force dark mode regardless of system settings
    darkTheme: Boolean = true,
    // Dynamic color is available on Android 12+, but we disable it to enforce Shadcn palette
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        else -> ShadcnDarkModeColors
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = ShadcnTypography,
        shapes = ShadcnShapes,
        content = content
    )
}
