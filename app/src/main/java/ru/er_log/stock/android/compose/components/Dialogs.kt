package ru.er_log.stock.android.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import ru.er_log.stock.android.compose.theme.AppTheme

@Composable
private fun ProgressIndicatorDialog() {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Box(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(4.dp))
                .background(AppTheme.colors.background)
                .padding(10.dp)
        ) {
            CircularProgressIndicator(color = AppTheme.colors.primary)
        }
    }
}