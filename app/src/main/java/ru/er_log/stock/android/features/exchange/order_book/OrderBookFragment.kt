package ru.er_log.stock.android.features.exchange.order_book

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.er_log.stock.android.R
import ru.er_log.stock.android.databinding.FragmentOrderBookBinding


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
        _binding = FragmentOrderBookBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerViewPurchase = binding.recyclerViewPurchase
        recyclerViewPurchase.layoutManager = LinearLayoutManager(context)
        val purchaseAdapter = LotRecyclerAdapter()
        recyclerViewPurchase.adapter = purchaseAdapter

        val recyclerViewSale = binding.recyclerViewSale
        recyclerViewSale.layoutManager = LinearLayoutManager(context)
        val saleAdapter = LotRecyclerAdapter()
        recyclerViewSale.adapter = saleAdapter

        lifecycleScope.launchWhenCreated {
            viewModel.lotsPurchase.collect { purchaseAdapter.submitList(it) }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.lotsSale.collect { saleAdapter.submitList(it) }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.errors.collect(::handleError)
        }

        viewModel.loadActiveLots()

        setHasOptionsMenu(true)
        return root
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