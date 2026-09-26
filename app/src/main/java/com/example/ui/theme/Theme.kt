package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = MintNeon,
    onPrimary = Color(0xFF003824),
    primaryContainer = Color(0xFF005236),
    onPrimaryContainer = MintLight,
    secondary = CyanNeon,
    onSecondary = Color(0xFF003648),
    secondaryContainer = Color(0xFF004D65),
    onSecondaryContainer = Color(0xFFBBE9FF),
    tertiary = GoldMedal,
    background = DarkBg,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkCard,
    onSurfaceVariant = TextSecondary,
    outline = DarkCardBorder,
    error = DangerCoral
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to dark as per screenshot
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
