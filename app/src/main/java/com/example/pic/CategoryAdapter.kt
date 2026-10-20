package com.example.pic

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.LinearProgressIndicator

class CategoryAdapter(
    private var categories: List<Category>
): ListAdapter<Category, CategoryAdapter.CategoryViewHolder>(CategoryDiffCallback()) {
    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvCategoryName: TextView = itemView.findViewById(R.id.tvCategoryName)
        val tvCategoryPercent: TextView = itemView.findViewById(R.id.tvCategoryPercent)
        val tvValue: TextView = itemView.findViewById(R.id.tvCategoryValue)
        val progressCategory: LinearProgressIndicator = itemView.findViewById(R.id.progressCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = categories[position]

        holder.tvCategoryName.text = item.name
        holder.tvCategoryPercent.text = "${item.percentage}%"
        holder.tvValue.text = String.format("R$ %.2f", item.amount)
        holder.progressCategory.progress = item.percentage

        try {
            val color = Color.parseColor(item.colorHex)
            holder.progressCategory.setIndicatorColor(color)
        } catch (e: IllegalArgumentException) {
            holder.progressCategory.setIndicatorColor(Color.parseColor("#8B3214"))
        }
    }

    override fun getItemCount(): Int = categories.size

    fun updateList(newList: List<Category>){
        this.categories = newList
        notifyDataSetChanged()
    }

    private class CategoryDiffCallback : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            // Unique identifier check (e.g., category name or ID)
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            // Checks if contents have changed
            return oldItem == newItem
        }
    }
}