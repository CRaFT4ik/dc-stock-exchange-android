package ru.er_log.stock.android.compose.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.er_log.stock.android.compose.theme.StockTheme

@Preview
@Composable
fun StockCardPreview() {
    StockCard {
        Column {
            Text("Text text text")
            StockOutlinedButton(onClick = {}) {
                Text("Test button")
            }
        }
    }
}

@Composable
fun StockCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(6.dp),
    color: Color = StockTheme.colors.surface,
    contentColor: Color = StockTheme.colors.textPrimary,
    border: BorderStroke? = null,
    elevation: Dp = 3.dp,
    content: @Composable () -> Unit
) {
    StockSurface(
        modifier = Modifier.padding(6.dp).then(modifier),
        shape = shape,
        color = color,
        contentColor = contentColor,
        border = border,
        elevation = elevation
    ) {
        content()
    }
}