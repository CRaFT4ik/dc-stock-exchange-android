package ru.er_log.stock.android.compose.components.order_book

import android.graphics.Rect
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.er_log.stock.android.R
import ru.er_log.stock.android.base.utils.autoScale
import ru.er_log.stock.android.base.utils.toHumanFormat
import ru.er_log.stock.android.compose.theme.StockTheme
import ru.er_log.stock.android.compose.theme.darkColors
import ru.er_log.stock.domain.models.order_book.OrderBookItem
import java.math.BigDecimal
import java.util.*


@Preview
@Composable
private fun OrderBookTablePreview() {
    val orderBookState = remember {
        val previewProvider = OrderBookPreviewProvider()
        OrderBookState(
            orders = previewProvider.provide(20000, 30000),
            offers = previewProvider.provide(30000, 40000)
        )
    }

    StockTheme(colors = darkColors()) {
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
        modifier = modifier.fillMaxSize()
    ) {
        OrderBookTableHeader(style = style)
        OrderBookTableColumns(
            ordersItems = remember {
                state.orders.toSortedSet(OrderBookItem.PriceDescComparator)
            },
            ordersMaxAmount = state.ordersMaxAmount,
            offersItems = state.offers,
            offersMaxAmount = state.offersMaxAmount,
            style = style
        )
    }
}

@Composable
private fun OrderBookTableHeader(style: OrderBookStyle) {
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

    val lineColor = style.secondaryColor
    Canvas(modifier = Modifier.fillMaxWidth()) {
        drawLine(
            color = lineColor.copy(alpha = 0.3f),
            strokeWidth = 1.dp.value,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f)
        )
    }
}

@Composable
private fun OrderBookTableColumns(
    modifier: Modifier = Modifier,
    ordersItems: SortedSet<OrderBookItem>,
    ordersMaxAmount: BigDecimal,
    offersItems: SortedSet<OrderBookItem>,
    offersMaxAmount: BigDecimal,
    style: OrderBookStyle
) {
    fun SortedSet<OrderBookItem>.toCountedList(): List<OrderBookCountedItem> {
        return mutableListOf<OrderBookCountedItem>().also { list ->
            fold(BigDecimal.ZERO) { acc, item ->
                val countedItem = OrderBookCountedItem(acc, item).also { list.add(it) }
                countedItem.totalAmount
            }
        }
    }

    val itemsList = remember {
        val ordersIterator = ordersItems.toCountedList().iterator()
        val offersIterator = offersItems.toCountedList().iterator()
        mutableListOf<Pair<OrderBookCountedItem, OrderBookCountedItem>>().apply {
            while (ordersIterator.hasNext() && offersIterator.hasNext()) {
                add(ordersIterator.next() to offersIterator.next())
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
                Row {
                    OrderBookTableListItem(
                        modifier = Modifier.weight(0.5f),
                        maxAmount = ordersMaxAmount,
                        item = it.first,
                        chartColor = style.ordersColor to style.ordersSecondaryColor,
                        style = style,
                        reversed = false
                    )
                    OrderBookTableListItem(
                        modifier = Modifier.weight(0.5f),
                        maxAmount = offersMaxAmount,
                        item = it.second,
                        chartColor = style.offersColor to style.offersSecondaryColor,
                        style = style,
                        reversed = true
                    )
                }
            }
        )
    }
}

@Composable
private fun OrderBookTableListItem(
    modifier: Modifier = Modifier,
    maxAmount: BigDecimal,
    item: OrderBookCountedItem,
    chartColor: Pair<Color, Color>,
    style: OrderBookStyle,
    reversed: Boolean
) {
    val bounds = Rect()
    val textPaint = Paint().asFrameworkPaint().apply {
        this.isAntiAlias = true
        this.textSize = with(LocalDensity.current) { style.textSize.toPx() }
        this.color = chartColor.first.toArgb()
    }

    val textColor = style.primaryColor
    Canvas(
        modifier = modifier
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
                textPaint.apply { color = textColor.toArgb() }
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