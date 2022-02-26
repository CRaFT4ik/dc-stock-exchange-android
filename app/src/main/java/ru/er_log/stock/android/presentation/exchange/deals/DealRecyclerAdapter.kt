package ru.er_log.stock.android.presentation.exchange.deals

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.er_log.stock.android.R
import ru.er_log.stock.android.databinding.DealItemBinding
import ru.er_log.stock.android.databinding.LotItemBinding
import ru.er_log.stock.domain.models.Deal
import ru.er_log.stock.domain.models.Lot
import java.text.SimpleDateFormat
import java.util.*

class DealRecyclerAdapter : ListAdapter<Deal, DealRecyclerAdapter.ViewHolder>(diffCallback) {
    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Deal>() {
            override fun areItemsTheSame(oldItem: Deal, newItem: Deal): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Deal, newItem: Deal): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DealItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    class ViewHolder(private val binding: DealItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(deal: Deal) {
            bindLot(binding.lotPurchase, deal.lotPurchase)
            bindLot(binding.lotSale, deal.lotSale)

            val context = itemView.context
            binding.dealCreatedDate.text = context.getString(R.string.deal_date, formatDate(deal.timestampCreated))
        }

        private fun bindLot(lotBinding: LotItemBinding, lot: Lot) {
            val context = itemView.context
            lotBinding.price.text = context.getString(R.string.lot_price, lot.price)
            lotBinding.createdDate.text = context.getString(R.string.lot_date, formatDate(lot.timestampCreated))
            lotBinding.owner.text = context.getString(R.string.lot_owner, lot.owner.name)
        }

        private fun formatDate(timestamp: Long): String {
            val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                itemView.context.resources.configuration.locales.get(0)
            } else {
                itemView.context.resources.configuration.locale
            }
            return SimpleDateFormat("dd.MM.yyyy HH:mm:ss", locale).format(Date(timestamp))
        }
    }
}