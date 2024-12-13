package com.serenity.serenityapp.ui.adapter

import android.graphics.Color
import android.provider.Telephony.Mms.Sent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.serenity.serenityapp.R
import com.serenity.serenityapp.data.model.api.response.ChallengeData
import com.serenity.serenityapp.data.model.database.Sentiment
import com.serenity.serenityapp.data.model.database.User
import com.serenity.serenityapp.data.model.database.UserWithSentiment
import com.serenity.serenityapp.databinding.ItemChallengeBinding
import com.serenity.serenityapp.databinding.ItemSentimentBinding

class SentimentRecycleViewAdapter(
    private val usersWithSentiments: List<UserWithSentiment>,
):
    RecyclerView.Adapter<SentimentRecycleViewAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemSentimentBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(userWithSentiment: UserWithSentiment) {
            var sentiment: Sentiment? = null
            if (userWithSentiment.sentiments.isNotEmpty()) {
                sentiment = userWithSentiment.sentiments.maxByOrNull { it.predictAt }
            }

            binding.tvUserName.text = userWithSentiment.user.name

            Glide.with(binding.root.context)
                .load(userWithSentiment.user.avatar())
                .into(binding.civPhoto)

            if (sentiment == null) {
                binding.tvSentiment.text = "\uD83D\uDE10"
                binding.tvSentimentText.text = "No Sentiment"
                binding.tvPredictedAt.text = "Not yet predicted"
            } else {
                binding.tvSentimentText.text = sentiment.sentiment
                binding.tvPredictedAt.text = sentiment.predictAtText()

                when (sentiment.sentiment) {
                    "Positive" -> {
                        binding.tvSentiment.text = "\uD83D\uDE0A"
                    }

                    "Neutral" -> {
                        binding.tvSentiment.text = "\uD83D\uDE10"
                    }

                    "Negative" -> {
                        binding.tvSentiment.text = "\uD83D\uDE21"
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): ViewHolder {
        val binding = ItemSentimentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder,position: Int) {
        holder.bind(usersWithSentiments[position])
    }

    override fun getItemCount(): Int {
        return usersWithSentiments.size
    }

}