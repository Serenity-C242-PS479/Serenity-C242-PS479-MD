package com.serenity.serenityapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.serenity.serenityapp.data.model.Activity
import com.serenity.serenityapp.data.model.Challenge
import com.serenity.serenityapp.databinding.ItemActivityRecentBinding
import com.serenity.serenityapp.databinding.ItemChallengeBinding

class ChallengeRecycleViewAdapter(private val challenges: List<Challenge>):
    RecyclerView.Adapter<ChallengeRecycleViewAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemChallengeBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(challenge: Challenge) {
            binding.tvName.text = challenge.name
            binding.tvCreatedAt.text = challenge.createdAt
            binding.tvDuration.text = "${challenge.hourStart} : ${challenge.hourEnd}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): ViewHolder {
        val binding = ItemChallengeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder,position: Int) {
        holder.bind(challenges[position])
    }

    override fun getItemCount(): Int {
        return challenges.size
    }

}