package ru.er_log.stock.android.compose.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
        modifier = modifier
            .fillMaxWidth(),
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