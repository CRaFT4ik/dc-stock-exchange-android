package ru.er_log.stock.android.features.order_book

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.koin.androidx.compose.getViewModel
import ru.er_log.stock.android.compose.components.order_book.OrderBookChart
import ru.er_log.stock.android.compose.components.order_book.OrderBookPreviewProvider
import ru.er_log.stock.android.compose.components.order_book.OrderBookState
import ru.er_log.stock.android.compose.components.order_book.OrderBookTable
import ru.er_log.stock.android.compose.theme.StockTheme
import ru.er_log.stock.android.compose.theme.darkColors

@Preview
@Composable
fun OrderBookScreenPreview() {
    StockTheme(colors = darkColors()) {
        OrderBookScreenImpl(state = OrderBookPreviewProvider().provideState())
    }
}

@Composable
fun OrderBookScreen(
    orderBookViewModel: OrderBookViewModel = getViewModel()
) {
    val orderBookState = orderBookViewModel.orderBookState.collectAsState(OrderBookState())
    OrderBookScreenImpl(state = orderBookState.value)
}

@Composable
private fun OrderBookScreenImpl(
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