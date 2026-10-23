package com.example.pic

import android.graphics.Color
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.size
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip

class HistoryAdapter(private val items: MutableList<HistoryItem>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvSubtitle: TextView = view.findViewById(R.id.tvSubtitle)
        val tvValue: TextView = view.findViewById(R.id.tvValue)
        val ivIcon: ImageView = view.findViewById(R.id.ivIcon)
        val layoutActions: LinearLayout = itemView.findViewById(R.id.layoutActions)
        val btnEdit: View = itemView.findViewById(R.id.btnEdit)
        val btnDelete: View = itemView.findViewById(R.id.btnDelete)

        val cardIcon: MaterialCardView = view.findViewById(R.id.cardIcon)

        init {
            // Set the click listener on the entire card (itemView)
            itemView.setOnClickListener {
                // Check if it's currently open
                val isCurrentlyExpanded = layoutActions.visibility == View.VISIBLE

                // This line adds a smooth slide animation when opening/closing
                TransitionManager.beginDelayedTransition(itemView as ViewGroup, AutoTransition())

                // Toggle visibility
                if (isCurrentlyExpanded) {
                    layoutActions.visibility = View.GONE
                } else {
                    layoutActions.visibility = View.VISIBLE
                }
            }

            // Handle your button clicks here
            btnEdit.setOnClickListener {

            }

            btnDelete.setOnClickListener {
                val position = bindingAdapterPosition

                if(position != RecyclerView.NO_POSITION){

                    items.removeAt(position)

                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, items.size)
                }
            }
        }
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

        holder.layoutActions.visibility = View.GONE

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
