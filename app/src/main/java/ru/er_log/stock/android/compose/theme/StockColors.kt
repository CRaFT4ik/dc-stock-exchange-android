package ru.er_log.stock.android.compose.theme

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

@Stable
class StockColors(
    primary: Color,
    secondary: Color,
    textPrimary: Color,
    textSecondary: Color,
    surface: Color,
    surfaceSecondary: Color,
    onSurface: Color,
    onSurfaceSecondary: Color,
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
    var onSurface by mutableStateOf(surfaceSecondary)
        private set
    var onSurfaceSecondary by mutableStateOf(surfaceSecondary)
        private set
    var background by mutableStateOf(background)
        private set
    var backgroundSecondary by mutableStateOf(backgroundSecondary)
        private set
    var error by mutableStateOf(error)
        private set
    var isLight by mutableStateOf(isLight)
        internal set

    fun updateColorsFrom(other: StockColors) {
        primary = other.primary
        secondary = other.secondary
        textPrimary = other.textPrimary
        textSecondary = other.textSecondary
        surface = other.surface
        surfaceSecondary = other.surfaceSecondary
        onSurface = other.onSurface
        onSurfaceSecondary = other.onSurfaceSecondary
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
        onSurface: Color = this.onSurface,
        onSurfaceSecondary: Color = this.onSurfaceSecondary,
        background: Color = this.background,
        backgroundSecondary: Color = this.backgroundSecondary,
        error: Color = this.error,
        isLight: Boolean = this.isLight
    ): StockColors = StockColors(
        primary = primary,
        secondary = secondary,
        textPrimary = textPrimary,
        textSecondary = textSecondary,
        surface = surface,
        surfaceSecondary = surfaceSecondary,
        onSurface = onSurface,
        onSurfaceSecondary = onSurfaceSecondary,
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
    textPrimary: Color = Color(0xFFC5C5C5),
    textSecondary: Color = Color(0x4DC4C4C4),
    surface: Color = Color(0xFF192839),
    surfaceSecondary: Color = Color(0xFF243A53),
    onSurface: Color = Color(0xFF345479),
    onSurfaceSecondary: Color = Color(0xFF29425F),
    background: Color = Color(0xFF131e2a),
    backgroundSecondary: Color = Color(0xFF0c151d),
    error: Color = Color(0xFFdf6060)
): StockColors = StockColors(
    primary = primary,
    secondary = secondary,
    textPrimary = textPrimary,
    textSecondary = textSecondary,
    surface = surface,
    surfaceSecondary = surfaceSecondary,
    onSurface = onSurface,
    onSurfaceSecondary = onSurfaceSecondary,
    background = background,
    backgroundSecondary = backgroundSecondary,
    error = error,
    isLight = true
)

fun darkColors(
    primary: Color = Color(0xFF0B66AC),
    secondary: Color = Color(0xFFF15033),
    textPrimary: Color = Color(0xFFC5C5C5),
    textSecondary: Color = Color(0xFF29425F),
    surface: Color = Color(0xFF192839),
    surfaceSecondary: Color = Color(0xFF243a53),
    onSurface: Color = Color(0xFF345479),
    onSurfaceSecondary: Color = Color(0xFF29425F),
    background: Color = Color(0xFF131e2a),
    backgroundSecondary: Color = Color(0xFF0c151d),
    error: Color = Color(0xFFdf6060)
): StockColors = StockColors(
    primary = primary,
    secondary = secondary,
    textPrimary = textPrimary,
    textSecondary = textSecondary,
    surface = surface,
    surfaceSecondary = surfaceSecondary,
    onSurface = onSurface,
    onSurfaceSecondary = onSurfaceSecondary,
    background = background,
    backgroundSecondary = backgroundSecondary,
    error = error,
    isLight = false
)

internal val LocalColors = staticCompositionLocalOf { lightColors() }