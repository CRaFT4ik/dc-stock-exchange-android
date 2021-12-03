package ru.er_log.stock.android.ui.exchange.deals

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collect
import ru.er_log.stock.android.databinding.FragmentCompletedDealsBinding
import java.io.IOException

class CompletedDealsFragment : Fragment() {

    private lateinit var viewModel: CompletedDealsViewModel
    private var _binding: FragmentCompletedDealsBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[CompletedDealsViewModel::class.java]

        _binding = FragmentCompletedDealsBinding.inflate(inflater, container, false)
        val root: View = binding.root
//
        val recyclerViewPurchase = binding.recyclerViewDeals
        recyclerViewPurchase.layoutManager = LinearLayoutManager(context)
        val dealsAdapter = DealRecyclerAdapter()
        recyclerViewPurchase.adapter = dealsAdapter

        lifecycleScope.launchWhenCreated {
            viewModel.deals.collect { dealsAdapter.submitList(it) }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.errors.collect(::handleError)
        }

        viewModel.loadCompletedDeals()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleError(error: Throwable) {
        Toast.makeText(context, error.localizedMessage, Toast.LENGTH_SHORT).show()
        Log.e("error", error.stackTraceToString())
    }
}