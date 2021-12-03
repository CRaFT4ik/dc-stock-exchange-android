package ru.er_log.stock.android.ui.exchange

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.er_log.stock.android.R
import ru.er_log.stock.domain.models.Lot
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
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.lot_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val price: TextView by lazy { itemView.findViewById(R.id.price) }
        private val date: TextView by lazy { itemView.findViewById(R.id.created_date) }
        private val owner: TextView by lazy { itemView.findViewById(R.id.owner) }

        fun bind(lot: Lot) {
            val context = itemView.context

            price.text = context.getString(R.string.lot_price, lot.price)
            date.text = context.getString(R.string.lot_date, formatDate(lot.timestampCreated))
            owner.text = context.getString(R.string.lot_owner, lot.owner.name)
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