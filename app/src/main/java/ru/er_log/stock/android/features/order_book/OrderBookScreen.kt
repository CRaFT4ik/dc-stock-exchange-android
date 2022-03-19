package ru.er_log.stock.android.features.order_book

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
        val state = OrderBookPreviewProvider().provideState()
        OrderBookScreenImpl(state = { state })
    }
}

@Composable
fun OrderBookScreen(
    orderBookViewModel: OrderBookViewModel = getViewModel()
) {
    val scope = rememberCoroutineScope()
    val orderBookState = remember { orderBookViewModel.orderBookState(scope) }.collectAsState()
    OrderBookScreenImpl(state = { orderBookState.value })
}

@Composable
private fun OrderBookScreenImpl(
    modifier: Modifier = Modifier,
    state: () -> OrderBookState
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
                .background(StockTheme.colors.background.copy(alpha = 0.3f))
                .weight(0.6f),
            state = state
        )
    }
}