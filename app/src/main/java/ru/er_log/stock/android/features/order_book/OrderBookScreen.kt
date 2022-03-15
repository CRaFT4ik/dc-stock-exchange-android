package ru.er_log.stock.android.features.order_book

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.koin.androidx.compose.getViewModel
import ru.er_log.stock.android.compose.components.order_book.OrderBookChart
import ru.er_log.stock.android.compose.components.order_book.OrderBookPreviewProvider
import ru.er_log.stock.android.compose.components.order_book.OrderBookState
import ru.er_log.stock.android.compose.components.order_book.OrderBookTable
import ru.er_log.stock.android.compose.theme.StockTheme
import ru.er_log.stock.android.compose.theme.darkColors

@Composable
fun OrderBookScreen(
    orderBookViewModel: OrderBookViewModel = getViewModel()
) {
    val orderBookState = orderBookViewModel.orderBookState.collectAsState(OrderBookState())
    OrderBookScreenContent(state = orderBookState.value)
}

@Preview
@Composable
fun OrderBookScreenPreview() {
    val orderBookState = remember {
        val previewProvider = OrderBookPreviewProvider()
        OrderBookState(
            orders = previewProvider.provide(20000, 30000),
            offers = previewProvider.provide(30000, 40000)
        )
    }

    StockTheme(colors = darkColors()) {
        OrderBookScreenContent(state = orderBookState)
    }
}

@Composable
private fun OrderBookScreenContent(
    modifier: Modifier = Modifier,
    state: OrderBookState
) {
    Column {
        OrderBookChart(
            modifier = modifier
                .background(StockTheme.colors.surface)
                .weight(0.4f),
            state = state
        )
        OrderBookTable(
            modifier = modifier
                .background(StockTheme.colors.background)
                .weight(0.6f),
            state = state
        )
    }
}