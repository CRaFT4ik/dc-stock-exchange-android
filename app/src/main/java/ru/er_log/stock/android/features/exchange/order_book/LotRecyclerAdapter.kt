package ru.er_log.stock.android.features.exchange.order_book

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.er_log.stock.android.R
import ru.er_log.stock.android.databinding.LotItemBinding
import ru.er_log.stock.domain.models.exchange.Lot
import java.text.SimpleDateFormat
import java.util.*

class LotRecyclerAdapter : ListAdapter<Lot, LotRecyclerAdapter.ViewHolder>(diffCallback) {
    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Lot>() {
            override fun areItemsTheSame(oldItem: Lot, newItem: Lot): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Lot, newItem: Lot): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LotItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    class ViewHolder(private val binding: LotItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(lot: Lot) {
            val context = itemView.context
            binding.price.text = context.getString(R.string.lot_price, lot.price)
            binding.createdDate.text = context.getString(R.string.lot_date, formatDate(lot.timestampCreated))
            binding.owner.text = context.getString(R.string.lot_owner, lot.owner.name)
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