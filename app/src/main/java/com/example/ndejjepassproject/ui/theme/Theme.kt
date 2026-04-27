package com.example.ndejjepassproject.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ── Light colour scheme ───────────────────────────────────────
private val LightColorScheme = lightColorScheme(
    primary            = Green600,
    onPrimary          = White,
    primaryContainer   = Green50,
    onPrimaryContainer = Green900,

    secondary          = Green400,
    onSecondary        = White,
    secondaryContainer = Green100,
    onSecondaryContainer = Green800,

    background         = Background,
    onBackground       = Green900,

    surface            = White,
    onSurface          = Green900,
    surfaceVariant     = Green50,
    onSurfaceVariant   = Green800,

    error              = Red600,
    onError            = White,
    errorContainer     = Red50,
    onErrorContainer   = Red600,
)

// ── Dark colour scheme ────────────────────────────────────────
private val DarkColorScheme = darkColorScheme(
    primary            = Green200,
    onPrimary          = Green900,
    primaryContainer   = Green800,
    onPrimaryContainer = Green50,

    secondary          = Green100,
    onSecondary        = Green900,
    secondaryContainer = Green800,
    onSecondaryContainer = Green100,

    background         = DarkSurface,
    onBackground       = Green50,

    surface            = Color(0xFF1F2B1C),
    onSurface          = Green50,
    surfaceVariant     = Green800,
    onSurfaceVariant   = Green100,

    error              = Red50,
    onError            = Red600,
    errorContainer     = Red600,
    onErrorContainer   = Red50,
)

// ── App theme ─────────────────────────────────────────────────
@Composable
fun NdejjeClearPassTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = Typography,
        content     = content
    )
}