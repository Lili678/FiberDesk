package com.example.fiberdesk_app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.fiberdesk_app.R
import com.example.fiberdesk_app.databinding.ItemRecentActivityBinding

data class RecentActivityItem(
    val title: String,
    val subtitle: String,
    val type: String, // "TICKET" o "INSTALACIÓN"
    val typeColor: Int
)

class RecentActivityAdapter(
    private var items: List<RecentActivityItem>,
    private val onItemClick: (RecentActivityItem) -> Unit
) : RecyclerView.Adapter<RecentActivityAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemRecentActivityBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecentActivityBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        
        holder.binding.apply {
            txtActivityTitle.text = item.title
            txtActivitySubtitle.text = item.subtitle
            txtActivityType.text = item.type
            txtActivityType.setBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, item.typeColor)
            )
            
            root.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<RecentActivityItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}
