package ru.er_log.stock.android.compose.theme

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

object StockTheme {
    /**
     * Retrieves the current [StockColors] at the call site's position in the hierarchy.
     */
    val colors: StockColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current

    /**
     * Retrieves the current [Typography] at the call site's position in the hierarchy.
     */
    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography

    /**
     * Retrieves the current [Shapes] at the call site's position in the hierarchy.
     */
    val shapes: Shapes
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.shapes
}

@Composable
fun StockTheme(
    colors: StockColors = if (isSystemInDarkTheme()) darkColors() else lightColors(),
    typography: Typography = StockTheme.typography,
    shapes: Shapes = StockTheme.shapes,
    content: @Composable () -> Unit
) {
    // creating a new object for colors to not mutate the initial colors set when updating the values
    val rememberedColors = remember { colors.copy() }.apply { updateColorsFrom(colors) }
    CompositionLocalProvider(
        LocalColors provides rememberedColors,
        LocalShapes provides shapes,
        LocalTypography provides typography,
        LocalIndication provides rememberRipple(color = StockTheme.colors.backgroundSecondary)
    ) {
        content()
    }
}

internal val LocalShapes = staticCompositionLocalOf { Shapes() }
internal val LocalTypography = staticCompositionLocalOf { Typography() }