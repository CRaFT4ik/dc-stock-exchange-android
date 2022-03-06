package ru.er_log.stock.android.compose.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.er_log.stock.android.compose.theme.AppTheme

@Composable
fun AppButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.wrapContentHeight(unbounded = true),
        colors = ButtonDefaults.buttonColors(
            contentColor = AppTheme.colors.textPrimary,
            backgroundColor = AppTheme.colors.surface,
            disabledContentColor = AppTheme.colors.textSecondary,
            disabledBackgroundColor = AppTheme.colors.surfaceSecondary
        ),
        shape = RoundedCornerShape(4.dp),
        contentPadding = PaddingValues(16.dp),
        enabled = enabled
    ) {
        content()
    }
}

@Composable
fun AppOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = AppTheme.colors.primary,
            backgroundColor = AppTheme.colors.surface.copy(alpha = 0.15f),
            disabledContentColor = AppTheme.colors.textSecondary,
        ),
        border = BorderStroke(
            ButtonDefaults.OutlinedBorderSize, AppTheme.colors.primary
        ),
        shape = RoundedCornerShape(4.dp),
        enabled = enabled
    ) {
        content()
    }
}