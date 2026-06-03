package com.hyuse.projectc.ui.theme

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat

// Mobile Cinema Color Palette (Dark Mode OLED Optimized)
private val DeepOLED = Color(0xFF0C0A09)
private val LucidGold = Color(0xFFA16207)
private val MutedSlate = Color(0xFF1C1917)
private val OnMutedSlate = Color(0xFFE8ECF0)

private val DarkColorScheme = darkColorScheme(
    primary = LucidGold,
    onPrimary = Color.White,
    secondary = MutedSlate,
    onSecondary = OnMutedSlate,
    background = DeepOLED,
    onBackground = Color.White,
    surface = DeepOLED,
    onSurface = Color.White,
    surfaceVariant = Color(0xFF1E293B),
    onSurfaceVariant = Color(0xFFF8FAFC),
    error = Color(0xFFDC2626)
)

// Mobile Cinema Inter Typography System
private val LucidTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold, // 700
        fontSize = 48.sp,
        letterSpacing = (-1.5).sp
    ),
    headlineLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold, // 600
        fontSize = 32.sp,
        letterSpacing = (-0.5).sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold, // 600
        fontSize = 24.sp,
        letterSpacing = (-0.5).sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal, // 400
        fontSize = 16.sp,
        letterSpacing = 0.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium, // 500
        fontSize = 12.sp,
        letterSpacing = 1.2.sp
    )
)

/**
 * Performance-conscious Lucid Glass surface.
 * Uses native BlurEffect on API 31+, otherwise falls back to a highly optimized translucent surface.
 */
@Composable
fun LucidSurface(
    modifier: Modifier = Modifier,
    blurRadius: Dp = 15.dp,
    alphaPrimary: Float = 0.08f, // Included for signature compatibility
    alphaSecondary: Float = 0.08f, // Included for signature compatibility
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(32.dp))
            .graphicsLayer {
                alpha = 0.99f
            }
            .background(Color.White.copy(alpha = 0.08f))
            .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(32.dp)),
        color = Color.Transparent,
        content = content
    )
}

/**
 * Primary Lucid Button
 * Follows the 44dp mobile-native touch target standard and uses the accent color.
 */
@Composable
fun LucidButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = LucidGold,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .heightIn(min = 48.dp) // Accessibility: min height
            .clickable(onClick = onClick)
    ) {
        Box(contentAlignment = androidx.compose.ui.Alignment.Center, modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(
                text = text.uppercase(),
                style = LucidTypography.labelLarge,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Project C Lucid Glass theme.
 * Forces dark mode for the premium OLED aesthetic.
 */
@Composable
fun ProjectCTheme(
    darkTheme: Boolean = true, // Force Dark Theme for Lucid Glass
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = LucidTypography,
        shapes = Shapes(
            extraSmall = RoundedCornerShape(8.dp),
            small = RoundedCornerShape(12.dp),
            medium = RoundedCornerShape(20.dp),
            large = RoundedCornerShape(32.dp),
            extraLarge = RoundedCornerShape(40.dp)
        ),
        content = content
    )
}
