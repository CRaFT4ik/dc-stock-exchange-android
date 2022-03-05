package ru.er_log.stock.android.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.*

object AppTheme {
    /**
     * Retrieves the current [AppColors] at the call site's position in the hierarchy.
     */
    val colors: AppColors
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
fun AppTheme(
    colors: AppColors = if (isSystemInDarkTheme()) darkColors() else lightColors(),
    typography: Typography = AppTheme.typography,
    shapes: Shapes = AppTheme.shapes,
    content: @Composable () -> Unit
) {
    // creating a new object for colors to not mutate the initial colors set when updating the values
    val rememberedColors = remember { colors.copy() }.apply { updateColorsFrom(colors) }
    CompositionLocalProvider(
        LocalColors provides rememberedColors,
        LocalShapes provides shapes,
        LocalTypography provides typography
    ) {
        content()
    }
}

internal val LocalShapes = staticCompositionLocalOf { Shapes() }
internal val LocalTypography = staticCompositionLocalOf { Typography() }