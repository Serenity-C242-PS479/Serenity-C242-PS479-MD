package com.serenity.serenityapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.serenity.serenityapp.R
import com.serenity.serenityapp.data.model.Activity
import com.serenity.serenityapp.data.model.Challenge
import com.serenity.serenityapp.databinding.FragmentHomeBinding
import com.serenity.serenityapp.ui.adapter.ActivityRecentRecycleViewAdapter
import com.serenity.serenityapp.ui.adapter.ChallengeRecycleViewAdapter
import com.serenity.serenityapp.ui.challenge.ChallengeFragment
import com.serenity.serenityapp.ui.login.LoginActivity
import com.serenity.serenityapp.ui.main.MainActivity
import com.serenity.serenityapp.ui.usage.UsageFragment

class HomeFragment: Fragment(R.layout.fragment_home), View.OnClickListener {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupOnClickListener()
        showRecentActivityRecycleView()
        showChallengeRecycleView()

        return binding.root
    }

    private fun setupOnClickListener() {
        binding.tvActivityMoreLabel.setOnClickListener(this)
        binding.tvChallengeMoreLabel.setOnClickListener(this)
    }

    private fun showRecentActivityRecycleView() {
        val dummyData = listOf(
            Activity(
                appId = "com.whatsapp",
                appName = "WhatsApp",
                usageTime = "2h 30m",
                lastUsedAt = "2024-11-23 20:45:00"
            ),
            Activity(
                appId = "com.instagram.android",
                appName = "Instagram",
                usageTime = "1h 15m",
                lastUsedAt = "2024-11-23 19:10:00"
            ),
            Activity(
                appId = "com.spotify.music",
                appName = "Spotify",
                usageTime = "3h 45m",
                lastUsedAt = "2024-11-23 21:00:00"
            ),
            Activity(
                appId = "com.google.android.youtube",
                appName = "YouTube",
                usageTime = "5h 20m",
                lastUsedAt = "2024-11-23 22:15:00"
            )
        )

        val adapter = ActivityRecentRecycleViewAdapter(dummyData)
        binding.rvActivity.adapter = adapter
        binding.rvActivity.layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.rvActivity.isNestedScrollingEnabled = false
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
        binding.rvActivity.isNestedScrollingEnabled = false
    }

    override fun onClick(view: View) {
        val activity = requireActivity() as MainActivity

        when(view) {
            binding.tvActivityMoreLabel -> {
                activity.changeSelectedNavigation(R.id.navigation_activity)
            }

            binding.tvChallengeMoreLabel -> {
                activity.changeSelectedNavigation(R.id.navigation_challenge)
            }
        }
    }
}