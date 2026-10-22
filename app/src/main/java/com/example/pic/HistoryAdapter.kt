package com.example.pic

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class HistoryAdapter(private val items: List<HistoryItem>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvSubtitle: TextView = view.findViewById(R.id.tvSubtitle)
        val tvValue: TextView = view.findViewById(R.id.tvValue)
        val ivIcon: ImageView = view.findViewById(R.id.ivIcon)

        val cardIcon: MaterialCardView = view.findViewById(R.id.cardIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = items[position]
        holder.tvTitle.text = item.title
        holder.tvSubtitle.text = item.subtitle
        holder.tvValue.text = item.value

        if (item.isExpense) {
            // If it's an expense (Despesa): Arrow pointing down, reddish/brown theme
            holder.ivIcon.setImageResource(R.drawable.arrow_down)
            holder.ivIcon.setColorFilter(Color.parseColor("#8D3C1B")) // Icon tint
            holder.tvValue.setTextColor(Color.parseColor("#8D3C1B"))    // Text color
            holder.cardIcon.setCardBackgroundColor(Color.parseColor("#FDF0E9")) // Background circle
        } else {
            // If it's a gain/income (Ganho): Arrow pointing up, green theme
            holder.ivIcon.setImageResource(R.drawable.arrow_upward)
            holder.ivIcon.setColorFilter(Color.parseColor("#047857"))
            holder.tvValue.setTextColor(Color.parseColor("#047857"))
            holder.cardIcon.setCardBackgroundColor(Color.parseColor("#E6F4EA"))

        }
    }

    override fun getItemCount(): Int = items.size
}
