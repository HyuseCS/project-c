package com.hyuse.projectc.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Custom color palette — modern, student-friendly tones
private val PrimaryLight = Color(0xFF4758A9)       // Indigo blue
private val OnPrimaryLight = Color(0xFFFFFFFF)
private val PrimaryContainerLight = Color(0xFFDDE1FF)
private val OnPrimaryContainerLight = Color(0xFF001257)

private val SecondaryLight = Color(0xFF5A5D72)
private val OnSecondaryLight = Color(0xFFFFFFFF)
private val SecondaryContainerLight = Color(0xFFDFE1F9)
private val OnSecondaryContainerLight = Color(0xFF171A2C)

private val TertiaryLight = Color(0xFF76546E)
private val OnTertiaryLight = Color(0xFFFFFFFF)
private val TertiaryContainerLight = Color(0xFFFFD7F2)
private val OnTertiaryContainerLight = Color(0xFF2D1228)

private val SurfaceLight = Color(0xFFFBF8FF)
private val OnSurfaceLight = Color(0xFF1B1B21)
private val SurfaceVariantLight = Color(0xFFE3E1EC)
private val OnSurfaceVariantLight = Color(0xFF46464F)

private val ErrorLight = Color(0xFFBA1A1A)

private val PrimaryDark = Color(0xFFB9C3FF)
private val OnPrimaryDark = Color(0xFF14296B)
private val PrimaryContainerDark = Color(0xFF2F4090)
private val OnPrimaryContainerDark = Color(0xFFDDE1FF)

private val SecondaryDark = Color(0xFFC3C5DD)
private val OnSecondaryDark = Color(0xFF2C2F42)
private val SecondaryContainerDark = Color(0xFF434559)
private val OnSecondaryContainerDark = Color(0xFFDFE1F9)

private val TertiaryDark = Color(0xFFE5BAD8)
private val OnTertiaryDark = Color(0xFF44263E)
private val TertiaryContainerDark = Color(0xFF5D3C55)
private val OnTertiaryContainerDark = Color(0xFFFFD7F2)

private val SurfaceDark = Color(0xFF131318)
private val OnSurfaceDark = Color(0xFFE4E1E9)
private val SurfaceVariantDark = Color(0xFF46464F)
private val OnSurfaceVariantDark = Color(0xFFC7C5D0)

private val ErrorDark = Color(0xFFFFB4AB)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryLight,
    onPrimary = OnPrimaryLight,
    primaryContainer = PrimaryContainerLight,
    onPrimaryContainer = OnPrimaryContainerLight,
    secondary = SecondaryLight,
    onSecondary = OnSecondaryLight,
    secondaryContainer = SecondaryContainerLight,
    onSecondaryContainer = OnSecondaryContainerLight,
    tertiary = TertiaryLight,
    onTertiary = OnTertiaryLight,
    tertiaryContainer = TertiaryContainerLight,
    onTertiaryContainer = OnTertiaryContainerLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    error = ErrorLight
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = OnPrimaryDark,
    primaryContainer = PrimaryContainerDark,
    onPrimaryContainer = OnPrimaryContainerDark,
    secondary = SecondaryDark,
    onSecondary = OnSecondaryDark,
    secondaryContainer = SecondaryContainerDark,
    onSecondaryContainer = OnSecondaryContainerDark,
    tertiary = TertiaryDark,
    onTertiary = OnTertiaryDark,
    tertiaryContainer = TertiaryContainerDark,
    onTertiaryContainer = OnTertiaryContainerDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    error = ErrorDark
)

/**
 * Project C Material 3 theme.
 * Supports dynamic color on Android 12+ and falls back to custom palette.
 */
@Composable
fun ProjectCTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        // Use Material You dynamic colors on Android 12+
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Set status bar color to match the theme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.surface.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
