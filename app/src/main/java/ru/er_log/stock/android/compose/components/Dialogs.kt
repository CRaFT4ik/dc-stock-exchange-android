package ru.er_log.stock.android.compose.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import ru.er_log.stock.android.compose.theme.StockTheme

@Composable
fun StockProgressIndicatorDialog() {
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
                .background(StockTheme.colors.background)
                .padding(10.dp)
        ) {
            CircularProgressIndicator(color = StockTheme.colors.primary)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun StockDialog(
    modifier: Modifier = Modifier,
    title: String? = null,
    onDismissRequest: () -> Unit = {},
    properties: DialogProperties = DialogProperties(
        dismissOnBackPress = true,
        dismissOnClickOutside = true,
        usePlatformDefaultWidth = false
    ),
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties
    ) {
        StockSurface(
            modifier = modifier
                .padding(24.dp)
                .widthIn(max = 400.dp),
            color = StockTheme.colors.surface,
            shape = RoundedCornerShape(6.dp),
        ) {
            val hPad = 12.dp
            val vPad = 8.dp
            Column(
                modifier = Modifier.padding(
                    top = if (title == null) vPad else 0.dp,
                    start = hPad,
                    end = hPad,
                    bottom = vPad
                )
            ) {
                if (title != null) {
                    val titleVPad = vPad * 1.5f
                    CompositionLocalProvider(LocalTextStyle provides StockTheme.typography.subtitle2) {
                        Text(
                            modifier = Modifier
                                .align(CenterHorizontally)
                                .padding(titleVPad),
                            text = title
                        )
                    }

                    val color = StockTheme.colors.onSurface
                    Canvas(modifier = modifier.fillMaxWidth().padding(bottom = titleVPad)) {
                        drawLine(color, Offset.Zero, Offset(size.width, 0f), strokeWidth = 1.dp.toPx())
                    }
                }
                content()
            }
        }
    }
}

@Preview
@Composable
fun StockDialogPreview() {
    StockDialog(
        title = "This is dialog title"
    ) {
        Text(text = "This is text text text text text text text...")
    }
}