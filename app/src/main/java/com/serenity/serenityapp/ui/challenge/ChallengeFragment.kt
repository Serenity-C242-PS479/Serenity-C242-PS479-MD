package com.serenity.serenityapp.ui.challenge

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.serenity.serenityapp.R
import com.serenity.serenityapp.data.model.Activity
import com.serenity.serenityapp.data.model.Challenge
import com.serenity.serenityapp.databinding.FragmentChallengeBinding
import com.serenity.serenityapp.databinding.FragmentUsageBinding
import com.serenity.serenityapp.ui.adapter.ActivityRecycleViewAdapter
import com.serenity.serenityapp.ui.adapter.ChallengeRecycleViewAdapter

class ChallengeFragment: Fragment(R.layout.fragment_challenge) {
    private lateinit var binding: FragmentChallengeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChallengeBinding.inflate(inflater, container, false)

        showChallengeRecycleView()

        return binding.root
    }

    private fun showChallengeRecycleView() {
        val dummyChallenges = listOf(
            Challenge(
                name = "Morning Workout",
                hourStart = "06:00",
                hourEnd = "07:00",
                createdAt = "2024-11-20 08:30:00"
            ),
            Challenge(
                name = "Reading Hour",
                hourStart = "09:00",
                hourEnd = "10:00",
                createdAt = "2024-11-21 10:15:00"
            ),
            Challenge(
                name = "Coding Challenge",
                hourStart = "14:00",
                hourEnd = "16:00",
                createdAt = "2024-11-22 14:45:00"
            ),
            Challenge(
                name = "Evening Run",
                hourStart = "17:30",
                hourEnd = "18:30",
                createdAt = "2024-11-23 17:00:00"
            )
        )

        val adapter = ChallengeRecycleViewAdapter(dummyChallenges)
        binding.rvChallenge.adapter = adapter
        binding.rvChallenge.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.rvChallenge.isNestedScrollingEnabled = false
    }
}