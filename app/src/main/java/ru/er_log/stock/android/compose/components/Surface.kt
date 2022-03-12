package ru.er_log.stock.android.compose.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.er_log.stock.android.compose.theme.AppTheme

@Preview
@Composable
fun AppSurfacePreview() {
    AppSurface {
        Column {
            Text("Text text text")
            AppOutlinedButton(onClick = {}) {
                Text("Test button")
            }
        }
    }
}

@Composable
fun AppSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    color: Color = AppTheme.colors.surface,
    contentColor: Color = AppTheme.colors.textPrimary,
    border: BorderStroke? = null,
    elevation: Dp = 6.dp,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = color,
        contentColor = contentColor,
        border = border,
        elevation = elevation
    ) {
//        Box(Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
            content()
//        }
    }
}