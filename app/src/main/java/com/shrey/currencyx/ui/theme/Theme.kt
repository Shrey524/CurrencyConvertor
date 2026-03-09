package com.shrey.currencyx.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val CurrencyXColorScheme = darkColorScheme(
    // Primary
    primary = CurrencyXColors.Teal,
    onPrimary = CurrencyXColors.Black,
    primaryContainer = CurrencyXColors.TealDark,
    onPrimaryContainer = CurrencyXColors.White,

    // Secondary
    secondary = CurrencyXColors.Cyan,
    onSecondary = CurrencyXColors.Black,
    secondaryContainer = CurrencyXColors.CyanDark,
    onSecondaryContainer = CurrencyXColors.White,

    // Tertiary
    tertiary = CurrencyXColors.TealLight,
    onTertiary = CurrencyXColors.Black,

    // Background & Surface
    background = CurrencyXColors.BlackSoft,
    onBackground = CurrencyXColors.White,
    surface = CurrencyXColors.BlackCard,
    onSurface = CurrencyXColors.White,
    surfaceVariant = CurrencyXColors.BlackElevated,
    onSurfaceVariant = CurrencyXColors.Gray600,

    // Outline
    outline = CurrencyXColors.Border,
    outlineVariant = CurrencyXColors.BorderLight,

    // Error
    error = CurrencyXColors.Error,
    onError = CurrencyXColors.White,
    errorContainer = CurrencyXColors.ErrorDark,
    onErrorContainer = CurrencyXColors.White,

    // Inverse
    inverseSurface = CurrencyXColors.Gray800,
    inverseOnSurface = CurrencyXColors.Black,
    inversePrimary = CurrencyXColors.TealDark,

    // Scrim
    scrim = CurrencyXColors.Black
)

@Composable
fun CurrencyXTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = CurrencyXColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = CurrencyXColors.BlackSoft.toArgb()
            window.navigationBarColor = CurrencyXColors.Black.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = false
                isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = CurrencyXTypography,
        shapes = CurrencyXShapes,
        content = content
    )
}
