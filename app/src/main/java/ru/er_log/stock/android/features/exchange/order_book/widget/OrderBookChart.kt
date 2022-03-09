package ru.er_log.stock.android.features.exchange.order_book.widget

import android.graphics.Rect
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.er_log.stock.android.R
import ru.er_log.stock.android.base.util.toHumanFormat
import ru.er_log.stock.android.compose.theme.AppTheme
import ru.er_log.stock.android.compose.theme.darkColors
import ru.er_log.stock.android.features.exchange.order_book.OrderBookScreen
import ru.er_log.stock.domain.models.exchange.OrderBookItem
import java.math.BigDecimal
import java.util.*
import kotlin.random.Random

@Composable
internal fun OrderBookChart(
    modifier: Modifier = Modifier,
    state: OrderBookState
) {
    val bounds = Rect()
    val textPaint = Paint().asFrameworkPaint().apply {
        isAntiAlias = true
        textSize = 32.sp.value
        color = AppTheme.colors.textPrimary.copy(alpha = 0.6f).toArgb()
    }

    fun Canvas.drawText(text: String, x: (Rect) -> Float, y: (Rect) -> Float) {
        textPaint.getTextBounds(text, 0, text.length, bounds)
        nativeCanvas.drawText(text, x(bounds), y(bounds), textPaint)
    }

    val ordersLegendText = stringResource(R.string.widget_order_book_legend_orders)
    val offersLegendText = stringResource(R.string.widget_order_book_legend_offers)

    val ordersColor = Color(0x4D00965A)
    val offersColor = Color(0x4DC74848)

    val chartMargin = 64.dp.value
    val priceBarHeight = 64.dp.value
    val legendBarHeight = 64.dp.value

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
    ) {
        val chartWidth = size.width
        val chartHeight = size.height - (chartMargin + priceBarHeight + legendBarHeight)

        val chartYOffset = chartMargin + chartHeight
        val priceYOffset = chartMargin + chartHeight + (priceBarHeight / 2f)
        val legendYOffset = chartMargin + chartHeight + priceBarHeight + (legendBarHeight / 2f)

        // Drawing amount lines.

        state.amountLines.forEach { amount ->
            val yOffset = chartYOffset - state.amountYOffset(chartHeight, amount)
            drawLine(
                color = Color.Gray.copy(alpha = 0.3f),
                strokeWidth = 1.dp.value,
                start = Offset(0f, yOffset),
                end = Offset(chartWidth, yOffset),
                pathEffect = PathEffect.dashPathEffect(
                    intervals = floatArrayOf(10f, 20f),
                    phase = 5f
                )
            )
        }

        // Drawing charts.

        fun drawChart(points: List<Offset>, color: Color) {
            drawOutline(
                outline = Outline.Generic(Path().apply {
                    points.firstOrNull()?.let { moveTo(it.x, it.y) }
                    points.forEach { lineTo(it.x, it.y) }
                    points.lastOrNull()?.let { lineTo(it.x, chartYOffset) }
                    points.firstOrNull()?.let { lineTo(it.x, chartYOffset) }
                    close()
                }),
                color = color.copy(alpha = 0.2f)
            )
            drawPoints(
                points,
                pointMode = PointMode.Polygon,
                strokeWidth = 5.dp.value,
                cap = StrokeCap.Round,
                color = color.copy(alpha = 0.7f)
            )
        }

        val pointsOrders =
            state.chartOrders(chartWidth.toInt(), chartHeight.toInt(), yOffset = chartMargin)
        drawChart(pointsOrders, color = ordersColor)

        val pointsOffers =
            state.chartOffers(chartWidth.toInt(), chartHeight.toInt(), yOffset = chartMargin)
        drawChart(pointsOffers, color = offersColor)

        // Drawing amount text.

        state.amountLines.forEach { amount ->
            val yOffset = chartYOffset - state.amountYOffset(chartHeight, amount)
            drawIntoCanvas {
                it.drawText(
                    amount.toHumanFormat(),
                    { 6.dp.value },
                    { b -> yOffset + (b.height() / 2f) }
                )
            }
        }

        // Drawing bottom price bar.

        drawIntoCanvas {
            it.drawText(
                state.priceMin.toString(),
                { 6.dp.value },
                { b -> priceYOffset + (b.height() / 2f) }
            )
            it.drawText(
                state.priceAvg.toString(),
                { b -> (chartWidth / 2f) - (b.width() / 2f) },
                { b -> priceYOffset + (b.height() / 2f) }
            )
            it.drawText(
                state.priceMax.toString(),
                { b -> chartWidth - b.width() - 6.dp.value },
                { b -> priceYOffset + (b.height() / 2f) }
            )
        }

        // Drawing chart legend.

        drawLegend(
            chartWidth, legendYOffset,
            ordersLegendText, offersLegendText,
            ordersColor, offersColor,
            textPaint
        )
    }
}

private fun DrawScope.drawLegend(
    chartWidth: Float, yOffset: Float,
    ordersText: String, offersText: String,
    ordersColor: Color, offersColor: Color,
    textPaint: NativePaint
) {
    val textOrdersBounds = Rect()
    val textOffersBounds = Rect()
    val smallPad = 12.dp.value
    val bigPad = 50.dp.value

    drawIntoCanvas {
        textPaint.getTextBounds(ordersText, 0, ordersText.length, textOrdersBounds)
        textPaint.getTextBounds(offersText, 0, offersText.length, textOffersBounds)

        val rectSize = textOrdersBounds.height()
        val totalSize = (smallPad * 2 + bigPad) +
                rectSize * 2 +
                textOrdersBounds.width() + textOffersBounds.width()
        var xOffset = (chartWidth - totalSize) / 2f

        drawRect(
            color = ordersColor,
            size = Size(rectSize.toFloat(), rectSize.toFloat()),
            topLeft = Offset(xOffset, yOffset - (rectSize + textOrdersBounds.bottom) / 2f)
        )
        xOffset += rectSize + smallPad

        it.nativeCanvas.drawText(
            ordersText,
            xOffset,
            yOffset + textOrdersBounds.height() / 2f - textOrdersBounds.bottom,
            textPaint
        )
        xOffset += textOrdersBounds.width() + bigPad

        drawRect(
            color = offersColor,
            size = Size(rectSize.toFloat(), rectSize.toFloat()),
            topLeft = Offset(xOffset, yOffset - (rectSize + textOffersBounds.bottom) / 2f)
        )
        xOffset += rectSize + smallPad

        it.nativeCanvas.drawText(
            offersText,
            xOffset,
            yOffset + textOffersBounds.height() / 2f - textOffersBounds.bottom,
            textPaint
        )
    }
}

@Preview
@Composable
fun OrderBookChartPreview() {
    val orderBookState = remember {
        OrderBookState(
            ordersState = mutableStateOf(lotsProvider(20000, 30000)),
            offersState = mutableStateOf(lotsProvider(30000, 40000))
        )
    }

    AppTheme(colors = darkColors()) {
        OrderBookScreen(orderBookState)
    }
}

val lotsProvider: (Int, Int) -> SortedSet<OrderBookItem> = { min, max ->
    val lots = sortedSetOf(OrderBookItem.PriceComparator)
    repeat(20) {
        lots.add(
            OrderBookItem(
                price = BigDecimal.valueOf(Random.nextDouble(min.toDouble(), max.toDouble())),
                amount = BigDecimal.valueOf(Random.nextDouble(0.01, 50.0))
            )
        )
    }
    lots
}