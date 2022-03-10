package ru.er_log.stock.android.features.home.exchange.order_book

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.er_log.stock.android.compose.theme.AppTheme
import ru.er_log.stock.android.compose.theme.darkColors
import ru.er_log.stock.android.features.home.exchange.order_book.widget.OrderBookChart
import ru.er_log.stock.android.features.home.exchange.order_book.widget.OrderBookPreviewProvider
import ru.er_log.stock.android.features.home.exchange.order_book.widget.OrderBookState
import ru.er_log.stock.android.features.home.exchange.order_book.widget.OrderBookTable

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
            modifier = modifier.weight(0.4f),
            state = state
        )
        OrderBookTable(
            modifier = modifier.weight(0.6f),
            state = state
        )
    }
}