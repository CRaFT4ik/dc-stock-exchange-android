package ru.er_log.stock.android.features.exchange.order_book.widget

import android.graphics.Rect
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.er_log.stock.android.compose.theme.AppTheme

@Composable
internal fun OrderBookChart(
    modifier: Modifier = Modifier,
    state: OrderBookState
) {
    val bounds = Rect()
    val textPaint = Paint().asFrameworkPaint().apply {
        isAntiAlias = true
        textSize = 35.sp.value
        color = AppTheme.colors.textPrimary.toArgb()
    }

    fun Canvas.drawText(text: String, x: (Rect) -> Float, y: (Rect) -> Float) {
        textPaint.getTextBounds(text, 0, text.length, bounds)
        nativeCanvas.drawText(text, x(bounds), y(bounds), textPaint)
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
    ) {
        val topMargin = 64.dp.value
        val priceBarHeight = 64.dp.value

        val chartWidth = size.width
        val chartHeight = size.height - priceBarHeight - topMargin

        fun drawPlot(points: List<Offset>, color: Color) {
            val chartY0 = topMargin + chartHeight
            drawOutline(
                outline = Outline.Generic(Path().apply {
                    points.firstOrNull()?.let { moveTo(it.x, it.y) }
                    points.forEach { lineTo(it.x, it.y) }
                    points.lastOrNull()?.let { lineTo(it.x, chartY0) }
                    points.firstOrNull()?.let { lineTo(it.x, chartY0) }
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
            state.chartOrders(chartWidth.toInt(), chartHeight.toInt(), yOffset = topMargin)
        drawPlot(pointsOrders, color = Color.Green)

        val pointsOffers =
            state.chartOffers(chartWidth.toInt(), chartHeight.toInt(), yOffset = topMargin)
        drawPlot(pointsOffers, color = Color.Red)

        val priceYOffset = topMargin + chartHeight + (priceBarHeight / 2f)
        drawIntoCanvas {
            it.drawText(
                state.priceMin.toString(),
                { 0f },
                { b -> priceYOffset + (b.height() / 2f) }
            )
            it.drawText(
                state.priceAvg.toString(),
                { b -> (chartWidth / 2f) - (b.width() / 2f) },
                { b -> priceYOffset + (b.height() / 2f) }
            )
            it.drawText(
                state.priceMax.toString(),
                { b -> chartWidth - b.width() },
                { b -> priceYOffset + (b.height() / 2f) }
            )
        }
    }
}