package com.shrey.currencyx.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * CurrencyX Color System
 * Inspired by: OpenAI, Anthropic, Vercel, Linear, Perplexity
 */
object CurrencyXColors {

    // ==========================================
    // BASE BLACKS
    // ==========================================
    val Black = Color(0xFF000000)
    val BlackPure = Color(0xFF050505)
    val BlackSoft = Color(0xFF0A0A0A)       // Main background
    val BlackCard = Color(0xFF111111)        // Card backgrounds
    val BlackElevated = Color(0xFF161616)    // Elevated surfaces
    val BlackInput = Color(0xFF0D0D0D)       // Input fields

    // ==========================================
    // BORDERS
    // ==========================================
    val Border = Color(0xFF1F1F1F)
    val BorderLight = Color(0xFF2A2A2A)
    val BorderSubtle = Color(0xFF1A1A1A)

    // ==========================================
    // GRAYS (Neutral scale)
    // ==========================================
    val Gray50 = Color(0xFF0A0A0A)
    val Gray100 = Color(0xFF171717)
    val Gray200 = Color(0xFF262626)
    val Gray300 = Color(0xFF404040)
    val Gray400 = Color(0xFF525252)
    val Gray500 = Color(0xFF737373)
    val Gray600 = Color(0xFFA3A3A3)
    val Gray700 = Color(0xFFD4D4D4)
    val Gray800 = Color(0xFFE5E5E5)
    val Gray900 = Color(0xFFF5F5F5)

    // ==========================================
    // WHITE
    // ==========================================
    val White = Color(0xFFFFFFFF)
    val WhiteMuted = Color(0xFFFAFAFA)
    val WhiteDim = Color(0xFFE0E0E0)

    // ==========================================
    // PRIMARY - TEAL
    // ==========================================
    val Teal = Color(0xFF14B8A6)
    val TealLight = Color(0xFF2DD4BF)
    val TealDark = Color(0xFF0D9488)
    val TealDarker = Color(0xFF0F766E)

    // Teal with alpha
    val TealMuted = Color(0x2614B8A6)        // 15% alpha
    val TealGlow = Color(0x4014B8A6)         // 25% alpha
    val TealSubtle = Color(0x1414B8A6)       // 8% alpha
    val TealBorder = Color(0x6614B8A6)       // 40% alpha

    // ==========================================
    // SECONDARY - CYAN
    // ==========================================
    val Cyan = Color(0xFF06B6D4)
    val CyanLight = Color(0xFF22D3EE)
    val CyanDark = Color(0xFF0891B2)
    val CyanMuted = Color(0x2606B6D4)
    val CyanGlow = Color(0x3306B6D4)

    // ==========================================
    // STATUS COLORS
    // ==========================================
    val Success = Color(0xFF22C55E)
    val SuccessLight = Color(0xFF4ADE80)
    val SuccessDark = Color(0xFF16A34A)
    val SuccessMuted = Color(0x2622C55E)

    val Error = Color(0xFFEF4444)
    val ErrorLight = Color(0xFFF87171)
    val ErrorDark = Color(0xFFDC2626)
    val ErrorMuted = Color(0x26EF4444)

    val Warning = Color(0xFFF59E0B)
    val WarningLight = Color(0xFFFBBF24)
    val WarningDark = Color(0xFFD97706)
    val WarningMuted = Color(0x26F59E0B)

    // ==========================================
    // SEMANTIC ALIASES (for easy theming)
    // ==========================================
    val Background = BlackSoft
    val Surface = BlackCard
    val SurfaceElevated = BlackElevated
    val OnBackground = White
    val OnSurface = White
    val OnSurfaceVariant = Gray500
    val Primary = Teal
    val OnPrimary = Black
    val Secondary = Cyan
    val Outline = Border
    val OutlineVariant = BorderLight

    // ==========================================
    // TEXT COLORS
    // ==========================================
    val TextPrimary = White
    val TextSecondary = Gray600
    val TextTertiary = Gray500
    val TextMuted = Gray400
    val TextDisabled = Gray300
    val TextLink = Teal
    val TextSuccess = Success
    val TextError = Error
    val TextWarning = Warning

    // ==========================================
    // COMPONENT-SPECIFIC
    // ==========================================

    // Buttons
    val ButtonPrimary = Teal
    val ButtonPrimaryPressed = TealDark
    val ButtonSecondary = Gray200
    val ButtonSecondaryPressed = Gray300
    val ButtonDisabled = Gray200
    val ButtonTextDisabled = Gray400

    // Inputs
    val InputBackground = BlackCard
    val InputBorder = Border
    val InputBorderFocused = Teal
    val InputText = White
    val InputPlaceholder = Gray500

    // Cards
    val CardBackground = BlackCard
    val CardBackgroundElevated = BlackElevated
    val CardBorder = Border
    val CardBorderHover = BorderLight

    // Navigation
    val NavBackground = BlackPure
    val NavItem = Gray500
    val NavItemActive = Teal
    val NavIndicator = Teal

    // Charts
    val ChartPositive = Teal
    val ChartNegative = Error
    val ChartGrid = Gray200
    val ChartLabel = Gray500
}

// -------------------------------------------------------------------
// Legacy color aliases (to keep existing code compiling)
// -------------------------------------------------------------------

val Emerald500 = CurrencyXColors.Teal
val Emerald600 = CurrencyXColors.TealDark
val Emerald700 = CurrencyXColors.TealDarker
val Emerald400 = CurrencyXColors.TealLight

val Slate900 = CurrencyXColors.BlackSoft
val Slate800 = CurrencyXColors.BlackCard
val Slate700 = CurrencyXColors.Gray200
val Slate600 = CurrencyXColors.Gray300
val Slate400 = CurrencyXColors.Gray500
val Slate300 = CurrencyXColors.Gray600
val Slate100 = CurrencyXColors.Gray800

val Yellow400 = CurrencyXColors.Warning
