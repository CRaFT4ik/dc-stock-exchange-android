package ru.er_log.stock.android.compose.theme

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

@Stable
class AppColors(
    primary: Color,
    secondary: Color,
    textPrimary: Color,
    textSecondary: Color,
    surface: Color,
    surfaceSecondary: Color,
    background: Color,
    backgroundSecondary: Color,
    error: Color,
    isLight: Boolean
) {
    var primary by mutableStateOf(primary)
        private set
    var secondary by mutableStateOf(secondary)
        private set
    var textPrimary by mutableStateOf(textPrimary)
        private set
    var textSecondary by mutableStateOf(textSecondary)
        private set
    var surface by mutableStateOf(surface)
        private set
    var surfaceSecondary by mutableStateOf(surfaceSecondary)
        private set
    var background by mutableStateOf(background)
        private set
    var backgroundSecondary by mutableStateOf(backgroundSecondary)
        private set
    var error by mutableStateOf(error)
        private set
    var isLight by mutableStateOf(isLight)
        internal set

    fun updateColorsFrom(other: AppColors) {
        primary = other.primary
        secondary = other.secondary
        textPrimary = other.textPrimary
        textSecondary = other.textSecondary
        surface = other.surface
        surfaceSecondary = other.surfaceSecondary
        background = other.background
        backgroundSecondary = other.backgroundSecondary
        error = other.error
        isLight = other.isLight
    }

    fun copy(
        primary: Color = this.primary,
        secondary: Color = this.secondary,
        textPrimary: Color = this.textPrimary,
        textSecondary: Color = this.textSecondary,
        surface: Color = this.surface,
        surfaceSecondary: Color = this.surfaceSecondary,
        background: Color = this.background,
        backgroundSecondary: Color = this.backgroundSecondary,
        error: Color = this.error,
        isLight: Boolean = this.isLight
    ): AppColors = AppColors(
        primary = primary,
        secondary = secondary,
        textPrimary = textPrimary,
        textSecondary = textSecondary,
        surface = surface,
        surfaceSecondary = surfaceSecondary,
        background,
        backgroundSecondary,
        error,
        isLight
    )

    companion object TradingColors {
        val ordersColor: Color = Color(0x4D00965A)
        val ordersSecondaryColor: Color = Color(0x7200965A)
        val offersColor: Color = Color(0x4DC74848)
        val offersSecondaryColor: Color = Color(0x72C74848)
        val progressColor: Color = Color(0xFFFF9800)
    }
}

fun lightColors(
    primary: Color = Color(0xFF0B66AC),
    secondary: Color = Color(0xFFF15033),
    textPrimary: Color = Color(0xFFFCFCFC),
    textSecondary: Color = Color(0x4DC4C4C4),
    surface: Color = Color(0xFF192839),
    surfaceSecondary: Color = Color(0xFF243a53),
    background: Color = Color(0xFF131e2a),
    backgroundSecondary: Color = Color(0xFF0c151d),
    error: Color = Color(0xFFdf6060)
): AppColors = AppColors(
    primary = primary,
    secondary = secondary,
    textPrimary = textPrimary,
    textSecondary = textSecondary,
    surface = surface,
    surfaceSecondary = surfaceSecondary,
    background = background,
    backgroundSecondary = backgroundSecondary,
    error = error,
    isLight = true
)

fun darkColors(
    primary: Color = Color(0xFF0B66AC),
    secondary: Color = Color(0xFFF15033),
    textPrimary: Color = Color(0xFFABBDD1),
    textSecondary: Color = Color(0xFF29425F),
    surface: Color = Color(0xFF192839),
    surfaceSecondary: Color = Color(0xFF243a53),
    background: Color = Color(0xFF131e2a),
    backgroundSecondary: Color = Color(0xFF0c151d),
    error: Color = Color(0xFFdf6060)
): AppColors = AppColors(
    primary = primary,
    secondary = secondary,
    textPrimary = textPrimary,
    textSecondary = textSecondary,
    surface = surface,
    surfaceSecondary = surfaceSecondary,
    background = background,
    backgroundSecondary = backgroundSecondary,
    error = error,
    isLight = false
)

internal val LocalColors = staticCompositionLocalOf { lightColors() }