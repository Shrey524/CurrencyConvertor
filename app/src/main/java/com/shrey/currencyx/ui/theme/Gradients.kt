package com.shrey.currencyx.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * App-wide gradient definitions
 */
object CurrencyXGradients {

    // Primary gradient (Teal to Cyan)
    val Primary = Brush.linearGradient(
        colors = listOf(
            CurrencyXColors.Teal,
            CurrencyXColors.Cyan
        )
    )

    val PrimaryHorizontal = Brush.horizontalGradient(
        colors = listOf(
            CurrencyXColors.Teal,
            CurrencyXColors.Cyan
        )
    )

    val PrimaryVertical = Brush.verticalGradient(
        colors = listOf(
            CurrencyXColors.Teal,
            CurrencyXColors.Cyan
        )
    )

    // Subtle background glow
    val TopGlow = Brush.verticalGradient(
        colors = listOf(
            CurrencyXColors.TealSubtle,
            Color.Transparent
        )
    )

    val BottomGlow = Brush.verticalGradient(
        colors = listOf(
            Color.Transparent,
            CurrencyXColors.TealSubtle
        )
    )

    // Card gradients
    val CardSurface = Brush.verticalGradient(
        colors = listOf(
            CurrencyXColors.BlackCard,
            CurrencyXColors.BlackSoft
        )
    )

    val CardElevated = Brush.linearGradient(
        colors = listOf(
            CurrencyXColors.Gray200,
            CurrencyXColors.Gray100
        )
    )

    // Border gradients
    val BorderPrimary = Brush.linearGradient(
        colors = listOf(
            CurrencyXColors.Teal,
            CurrencyXColors.Cyan
        )
    )

    val BorderSubtle = Brush.verticalGradient(
        colors = listOf(
            CurrencyXColors.BorderLight,
            CurrencyXColors.Border
        )
    )

    // Radial glow
    val RadialGlow = Brush.radialGradient(
        colors = listOf(
            CurrencyXColors.TealGlow,
            Color.Transparent
        )
    )

    // Success/Error gradients
    val Success = Brush.linearGradient(
        colors = listOf(
            CurrencyXColors.Success,
            CurrencyXColors.SuccessLight
        )
    )

    val Error = Brush.linearGradient(
        colors = listOf(
            CurrencyXColors.Error,
            CurrencyXColors.ErrorLight
        )
    )

    // Shimmer gradient (for loading states)
    fun shimmer(startX: Float) = Brush.linearGradient(
        colors = listOf(
            CurrencyXColors.Gray100,
            CurrencyXColors.Gray200,
            CurrencyXColors.Gray100
        ),
        start = Offset(startX - 200f, 0f),
        end = Offset(startX + 200f, 0f)
    )

    // Scrim/overlay
    val Scrim = Brush.verticalGradient(
        colors = listOf(
            Color.Transparent,
            CurrencyXColors.Black.copy(alpha = 0.8f)
        )
    )
}
