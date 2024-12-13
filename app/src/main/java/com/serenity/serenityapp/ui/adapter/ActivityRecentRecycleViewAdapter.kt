package com.serenity.serenityapp.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.serenity.serenityapp.data.model.Activity
import com.serenity.serenityapp.data.model.AppStat
import com.serenity.serenityapp.databinding.ItemActivityRecentBinding

class ActivityRecentRecycleViewAdapter(private val activities: List<AppStat>):
    RecyclerView.Adapter<ActivityRecentRecycleViewAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemActivityRecentBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(activity: AppStat) {
            val backgroundColor =  Color.parseColor(activity.cardBackgroundColor)
            val textColor =  Color.parseColor(activity.textColor)

            binding.tvAppName.text = activity.appName
            binding.tvLastUsed.text = activity.getFormatedLastTimeUsed()
            binding.tvUsage.text = activity.getFormatedTimeUsed()

            binding.root.setCardBackgroundColor(backgroundColor)

            binding.tvAppName.setTextColor(textColor)
            binding.tvLastUsed.setTextColor(textColor)
            binding.tvUsage.setTextColor(textColor)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): ViewHolder {
        val binding = ItemActivityRecentBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder,position: Int) {
        holder.bind(activities[position])
    }

    override fun getItemCount(): Int {
        return activities.size
    }

}