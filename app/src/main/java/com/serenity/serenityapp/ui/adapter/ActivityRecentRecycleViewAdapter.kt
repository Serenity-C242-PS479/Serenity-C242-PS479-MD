package com.serenity.serenityapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.serenity.serenityapp.data.model.Activity
import com.serenity.serenityapp.databinding.ItemActivityRecentBinding

class ActivityRecentRecycleViewAdapter(private val activities: List<Activity>):
    RecyclerView.Adapter<ActivityRecentRecycleViewAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemActivityRecentBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(activity: Activity) {
            binding.tvAppName.text = activity.appName
            binding.tvLastUsed.text = activity.lastUsedAt
            binding.tvUsage.text = activity.usageTime
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): ViewHolder {
        val binding = ItemActivityRecentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        val height = parent.measuredHeight
//        val width = parent.measuredWidth / 2
//
//        binding.root.layoutParams = RecyclerView.LayoutParams(width, height)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder,position: Int) {
        holder.bind(activities[position])
    }

    override fun getItemCount(): Int {
        return activities.size
    }

}