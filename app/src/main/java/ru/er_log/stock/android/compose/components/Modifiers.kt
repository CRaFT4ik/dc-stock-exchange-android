package ru.er_log.stock.android.compose.components

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Brush
import ru.er_log.stock.android.compose.theme.StockTheme

internal fun Modifier.stockMainBackground() = composed {
    this.background(
        brush = Brush.verticalGradient(
            colors = listOf(
                StockTheme.colors.backgroundSecondary,
                StockTheme.colors.background
            )
        )
    )
}