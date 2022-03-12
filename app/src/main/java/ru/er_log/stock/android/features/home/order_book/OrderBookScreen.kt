package ru.er_log.stock.android.features.home.order_book

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.er_log.stock.android.compose.components.order_book.OrderBookChart
import ru.er_log.stock.android.compose.components.order_book.OrderBookPreviewProvider
import ru.er_log.stock.android.compose.components.order_book.OrderBookState
import ru.er_log.stock.android.compose.components.order_book.OrderBookTable
import ru.er_log.stock.android.compose.theme.AppTheme
import ru.er_log.stock.android.compose.theme.darkColors

@Composable
fun OrderBookScreen(
    orderBookState: OrderBookState
) {
    OrderBook(state = orderBookState)
}

@Preview
@Composable
fun OrderBookScreenPreview() {
    val orderBookState = remember {
        val previewProvider = OrderBookPreviewProvider()
        OrderBookState(
            ordersState = mutableStateOf(previewProvider.provide(20000, 30000)),
            offersState = mutableStateOf(previewProvider.provide(30000, 40000))
        )
    }

    AppTheme(colors = darkColors()) {
        OrderBookScreen(orderBookState)
    }
}

@Composable
fun OrderBook(
    modifier: Modifier = Modifier,
    state: OrderBookState
) {
    Column {
        OrderBookChart(
            modifier = modifier
                .background(AppTheme.colors.surface)
                .weight(0.4f),
            state = state
        )
        OrderBookTable(
            modifier = modifier
                .background(AppTheme.colors.background)
                .weight(0.6f),
            state = state
        )
    }
}