package com.serenity.serenityapp.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.serenity.serenityapp.R
import com.serenity.serenityapp.data.model.api.response.ChallengeData
import com.serenity.serenityapp.databinding.ItemChallengeBinding

class ChallengeRecycleViewAdapter(
    private val challenges: List<ChallengeData>,
    private val isDeletable: Boolean = false,
    private val deleteCallback: ((ChallengeData) -> Unit)? = null
):
    RecyclerView.Adapter<ChallengeRecycleViewAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemChallengeBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(challenge: ChallengeData, isDeletable: Boolean, deleteCallback: ((ChallengeData) -> Unit)? = null) {
            binding.tvName.text = challenge.title
            binding.tvCreatedAt.text = challenge.createdAtText()
            binding.tvDuration.text = "${challenge.startHour()} : ${challenge.endHour()}"

            binding.tvDelete.visibility = if (isDeletable) View.VISIBLE else View.GONE
            binding.tvDelete.setOnClickListener {
                deleteCallback?.invoke(challenge)
            }

            when (challenge.status) {
                "On Progress" -> {
                    binding.ivStatus.setImageResource(R.drawable.ic_time)
                    val tintColor = Color.parseColor("#3A589C")
                    binding.ivStatus.setColorFilter(tintColor, android.graphics.PorterDuff.Mode.SRC_IN)
                }

                "Passed" -> {
                    binding.ivStatus.setImageResource(R.drawable.ic_check)
                    val tintColor = Color.parseColor("#00BFA6")
                    binding.ivStatus.setColorFilter(tintColor, android.graphics.PorterDuff.Mode.SRC_IN)
                }

                "Failed" -> {
                    binding.ivStatus.setImageResource(R.drawable.ic_close)
                    val tintColor = Color.parseColor("#FF0000")
                    binding.ivStatus.setColorFilter(tintColor, android.graphics.PorterDuff.Mode.SRC_IN)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): ViewHolder {
        val binding = ItemChallengeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder,position: Int) {
        holder.bind(challenges[position], isDeletable, deleteCallback)
    }

    override fun getItemCount(): Int {
        return challenges.size
    }

}