package ru.er_log.stock.android.features.home

import androidx.compose.material.BottomAppBar
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import ru.er_log.stock.android.base.utils.Navigator
import ru.er_log.stock.android.features.home.exchange.order_book.OrderBookScreen
import ru.er_log.stock.android.compose.components.order_book.OrderBookPreviewProvider
import ru.er_log.stock.android.compose.components.order_book.OrderBookState

@Composable
fun HomeScreen(navigator: Navigator) {
    val orderBookState = remember {
        val provider = OrderBookPreviewProvider()
        OrderBookState(
            mutableStateOf(provider.provide(10000, 20000)),
            mutableStateOf(provider.provide(20000, 30000))
        )
    }

    Scaffold(
        bottomBar = {
            BottomAppBar {
                Text("1")
                Text("2")
                Text("3")
            }
        }
    ) {
        OrderBookScreen(orderBookState)
    }
}