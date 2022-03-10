package ru.er_log.stock.android.features.exchange.order_book.widget

import android.graphics.Rect
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.er_log.stock.android.R
import ru.er_log.stock.android.base.util.autoScale
import ru.er_log.stock.android.base.util.spToPx
import ru.er_log.stock.android.base.util.toHumanFormat
import ru.er_log.stock.android.compose.theme.AppTheme
import ru.er_log.stock.android.compose.theme.darkColors
import ru.er_log.stock.domain.models.exchange.OrderBookItem
import java.math.BigDecimal
import java.util.*


@Preview
@Composable
private fun OrderBookTablePreview() {
    val orderBookState = remember {
        val previewProvider = OrderBookPreviewProvider()
        OrderBookState(
            ordersState = mutableStateOf(previewProvider.provide(20000, 30000)),
            offersState = mutableStateOf(previewProvider.provide(30000, 40000))
        )
    }

    AppTheme(colors = darkColors()) {
        OrderBookTable(state = orderBookState)
    }
}

@Composable
internal fun OrderBookTable(
    modifier: Modifier = Modifier,
    state: OrderBookState,
    style: OrderBookStyle = OrderBookStyle()
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
    ) {
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CompositionLocalProvider(
                LocalTextStyle provides TextStyle.Default.copy(
                    color = style.secondaryColor,
                    fontSize = style.textSize
                )
            ) {
                Text(text = stringResource(R.string.widget_order_book_table_total))
                Text(text = stringResource(R.string.widget_order_book_table_price))
                Text(text = stringResource(R.string.widget_order_book_table_total))
            }
        }

        Canvas(modifier = Modifier.fillMaxWidth()) {
            drawLine(
                color = style.secondaryColor.copy(alpha = 0.3f),
                strokeWidth = 1.dp.value,
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f)
            )
        }

        Row(modifier = Modifier.fillMaxSize()) {
            OrderBookColumn(
                modifier = Modifier.weight(0.5f),
                state.ordersMaxAmount,
                items = state.ordersState.value,
                chartColor = style.ordersColor to style.ordersSecondaryColor,
                style = style
            )
            OrderBookColumn(
                modifier = Modifier.weight(0.5f),
                state.offersMaxAmount,
                items = state.offersState.value,
                chartColor = style.offersColor to style.offersSecondaryColor,
                style = style,
                reversed = true
            )
        }
    }
}

@Composable
private fun OrderBookColumn(
    modifier: Modifier = Modifier,
    maxAmount: BigDecimal,
    items: SortedSet<OrderBookItem>,
    chartColor: Pair<Color, Color>,
    style: OrderBookStyle,
    reversed: Boolean = false
) {
    val itemsList = remember {
        mutableListOf<OrderBookCountedItem>().apply {
            items.fold(BigDecimal.ZERO) { acc, item ->
                val countedItem = OrderBookCountedItem(acc, item).also { add(it) }
                countedItem.totalAmount
            }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        items(
            items = itemsList,
            itemContent = {
                OrderBookColumnListItem(
                    maxAmount,
                    item = it,
                    chartColor = chartColor,
                    style = style,
                    reversed = reversed
                )
            }
        )
    }
}

@Composable
private fun OrderBookColumnListItem(
    maxAmount: BigDecimal,
    item: OrderBookCountedItem,
    chartColor: Pair<Color, Color>,
    style: OrderBookStyle,
    reversed: Boolean
) {
    val bounds = Rect()
    val textPaint = Paint().asFrameworkPaint().apply {
        this.isAntiAlias = true
        this.textSize = style.textSize.spToPx(LocalContext.current)
        this.color = chartColor.first.toArgb()
    }

    Canvas(
        modifier = Modifier
            .height(20.dp)
            .fillMaxWidth()
    ) {
        val width = size.width
        val height = size.height

        val xOffset = width - width * (item.totalAmount / maxAmount).toFloat()

        drawOutline(
            outline = Outline.Generic(Path().apply {
                drawRect(
                    chartColor.first,
                    size = Size(width - xOffset, height),
                    topLeft = if (!reversed) Offset(xOffset, 0f) else Offset(0f, 0f)
                )
            }),
            color = chartColor.first.copy(alpha = 0.2f)
        )

        val textPad = 16.dp.value
        drawIntoCanvas {
            val text = item.orderBookItem.price.autoScale().toString()
            textPaint.getTextBounds(text, 0, text.length, bounds)
            it.nativeCanvas.drawText(
                text,
                if (!reversed) width - bounds.width() - textPad else textPad,
                height - bounds.height() / 2f,
                textPaint.apply { color = chartColor.second.toArgb() }
            )
        }

        drawIntoCanvas {
            val text = item.totalAmount.toHumanFormat()
            textPaint.getTextBounds(text, 0, text.length, bounds)
            it.nativeCanvas.drawText(
                text,
                if (!reversed) textPad else width - bounds.width() - textPad,
                height - bounds.height() / 2f,
                textPaint.apply { color = style.primaryColor.toArgb() }
            )
        }
    }
}

private data class OrderBookCountedItem(
    val prevAmount: BigDecimal,
    val orderBookItem: OrderBookItem
) {
    val totalAmount: BigDecimal
        get() = prevAmount + orderBookItem.price * orderBookItem.amount
}