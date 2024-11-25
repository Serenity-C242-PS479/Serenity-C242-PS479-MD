package com.serenity.serenityapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.serenity.serenityapp.data.model.Activity
import com.serenity.serenityapp.data.model.AppLimit
import com.serenity.serenityapp.databinding.ItemActivityBinding
import com.serenity.serenityapp.databinding.ItemActivityRecentBinding
import com.serenity.serenityapp.databinding.ItemAppLimitBinding

class AppLimtRecycleViewAdapter(private val appLimits: List<AppLimit>):
    RecyclerView.Adapter<AppLimtRecycleViewAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemAppLimitBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(appLimit: AppLimit) {
            binding.tvAppName.text = appLimit.appName
            binding.edtHour.setText(appLimit.hour)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): ViewHolder {
        val binding = ItemAppLimitBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder,position: Int) {
        holder.bind(appLimits[position])
    }

    override fun getItemCount(): Int {
        return appLimits.size
    }

}