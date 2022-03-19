package ru.er_log.stock.android.compose.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.er_log.stock.android.compose.theme.StockTheme

@Composable
fun StockButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.wrapContentHeight(unbounded = true),
        colors = ButtonDefaults.buttonColors(
            contentColor = StockTheme.colors.textPrimary,
            backgroundColor = StockTheme.colors.surface,
            disabledContentColor = StockTheme.colors.textSecondary,
            disabledBackgroundColor = StockTheme.colors.background.copy(alpha = 0.45f)
        ),
        shape = RoundedCornerShape(4.dp),
        border = if (!enabled) BorderStroke(1.dp, color = StockTheme.colors.surface) else null,
        contentPadding = PaddingValues(16.dp),
        enabled = enabled
    ) {
        content()
    }
}

@Composable
fun StockOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = StockTheme.colors.primary,
            backgroundColor = StockTheme.colors.surface.copy(alpha = 0.4f),
            disabledContentColor = StockTheme.colors.textSecondary,
        ),
        border = BorderStroke(
            ButtonDefaults.OutlinedBorderSize,
            if (enabled) StockTheme.colors.primary else StockTheme.colors.onSurface
        ),
        shape = RoundedCornerShape(4.dp),
        enabled = enabled
    ) {
        content()
    }
}

@Composable
fun StockTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    TextButton(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        content = content,
        colors = ButtonDefaults.buttonColors(
            contentColor = StockTheme.colors.textPrimary,
            backgroundColor = StockTheme.colors.surface,
            disabledContentColor = StockTheme.colors.textSecondary,
            disabledBackgroundColor = StockTheme.colors.surfaceSecondary
        )
    )
}