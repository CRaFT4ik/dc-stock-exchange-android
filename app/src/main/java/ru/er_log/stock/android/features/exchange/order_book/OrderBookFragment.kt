package ru.er_log.stock.android.features.exchange.order_book

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.er_log.stock.android.R
import ru.er_log.stock.android.compose.theme.AppTheme
import ru.er_log.stock.android.compose.theme.darkColors
import ru.er_log.stock.android.databinding.FragmentOrderBookBinding
import ru.er_log.stock.android.features.exchange.order_book.widget.OrderBookChart
import ru.er_log.stock.android.features.exchange.order_book.widget.OrderBookState
import ru.er_log.stock.android.features.exchange.order_book.widget.OrderBookTable
import ru.er_log.stock.domain.models.exchange.OrderBookItem
import java.math.BigDecimal
import java.util.*
import kotlin.random.Random


class OrderBookFragment : Fragment(R.layout.fragment_order_book) {

    private val viewModel: OrderBookViewModel by viewModel()
    private var _binding: FragmentOrderBookBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme(colors = darkColors()) { // FIXME: remove colors
                    val orderBookState = remember {
                        OrderBookState(
                            ordersState = mutableStateOf(lotsProvider(20000, 30000)),
                            offersState = mutableStateOf(lotsProvider(30000, 40000))
                        )
                    }

                    val scope = LocalLifecycleOwner.current.lifecycleScope
                    LaunchedEffect(key1 = scope) {
                        scope.launch {
                            repeat(10) {
                                delay(2000)
                                orderBookState.ordersState.value = lotsProvider(20000, 30000)
                                orderBookState.offersState.value = lotsProvider(30000, 40000)
                            }
                        }
                    }

                    OrderBookScreen(orderBookState)
                }
            }
        }
//        _binding = FragmentOrderBookBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        val recyclerViewPurchase = binding.recyclerViewPurchase
//        recyclerViewPurchase.layoutManager = LinearLayoutManager(context)
//        val purchaseAdapter = LotRecyclerAdapter()
//        recyclerViewPurchase.adapter = purchaseAdapter
//
//        val recyclerViewSale = binding.recyclerViewSale
//        recyclerViewSale.layoutManager = LinearLayoutManager(context)
//        val saleAdapter = LotRecyclerAdapter()
//        recyclerViewSale.adapter = saleAdapter
//
//        lifecycleScope.launchWhenCreated {
//            viewModel.lotsPurchase.collect { purchaseAdapter.submitList(it) }
//        }
//
//        lifecycleScope.launchWhenCreated {
//            viewModel.lotsSale.collect { saleAdapter.submitList(it) }
//        }
//
//        lifecycleScope.launchWhenCreated {
//            viewModel.errors.collect(::handleError)
//        }
//
//        viewModel.loadActiveLots()
//
//        setHasOptionsMenu(true)
//        return root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_sync -> {
                viewModel.loadActiveLots()
                Toast.makeText(context, "Updating...", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleError(error: Throwable) {
        Toast.makeText(context, error.localizedMessage, Toast.LENGTH_SHORT).show()
        Log.e(this::class.simpleName, error.stackTraceToString())
    }
}

@Composable
fun OrderBookScreen(
    orderBookState: OrderBookState
) {
    OrderBook(orderBookState)
}

val lotsProvider: (Int, Int) -> SortedSet<OrderBookItem> = { min, max ->
    val lots = sortedSetOf(OrderBookItem.PriceComparator)
    repeat(20) {
        lots.add(
            OrderBookItem(
                price = BigDecimal.valueOf(Random.nextDouble(min.toDouble(), max.toDouble())),
                amount = BigDecimal.valueOf(Random.nextDouble(0.1, 50.0))
            )
        )
    }
    lots
}

@Preview
@Composable
fun OrderBookScreenPreview() {
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

@Composable
fun OrderBook(
    orderBookState: OrderBookState
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .background(AppTheme.colors.background)
    ) {
        OrderBookChart(
            modifier = Modifier.weight(0.5f),
            state = orderBookState
        )
        OrderBookTable(
            modifier = Modifier.weight(0.5f),
            state = orderBookState
        )
    }
}