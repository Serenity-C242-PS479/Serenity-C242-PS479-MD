package com.serenity.serenityapp.ui.usage

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.serenity.serenityapp.R
import com.serenity.serenityapp.data.model.Activity
import com.serenity.serenityapp.data.model.Challenge
import com.serenity.serenityapp.databinding.FragmentHomeBinding
import com.serenity.serenityapp.databinding.FragmentUsageBinding
import com.serenity.serenityapp.ui.adapter.ActivityRecentRecycleViewAdapter
import com.serenity.serenityapp.ui.adapter.ActivityRecycleViewAdapter
import com.serenity.serenityapp.ui.adapter.ChallengeRecycleViewAdapter

class UsageFragment: Fragment(R.layout.fragment_usage) {
    private lateinit var binding: FragmentUsageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUsageBinding.inflate(inflater, container, false)

        showActivityChart()
        showActivityRecycleView()

        return binding.root
    }

    private fun showActivityChart() {
        // Data untuk PieChart
        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(40f, "Facebook"))
        entries.add(PieEntry(30f, "TikTok"))
        entries.add(PieEntry(20f, "Instagram"))
        entries.add(PieEntry(10f, "X (Twitter)"))

        // Dataset
        val pieDataSet = PieDataSet(entries, "Categories")
        pieDataSet.colors = listOf(
            ContextCompat.getColor(requireActivity(),R.color.chartColor1),
            ContextCompat.getColor(requireActivity(),R.color.chartColor2),
            ContextCompat.getColor(requireActivity(),R.color.chartColor3),
            ContextCompat.getColor(requireActivity(),R.color.chartColor4)
        )
        pieDataSet.valueTextColor = Color.WHITE
        pieDataSet.valueTextSize = 12f

        // Data ke PieChart
        val pieData = PieData(pieDataSet)
        binding.chartActivity.data = pieData

        // Atur Deskripsi
        binding.chartActivity.description.isEnabled = false
        binding.chartActivity.setCenterTextSize(16f)
        binding.chartActivity.legend.isEnabled = false

        // Animasikan PieChart
        binding.chartActivity.animateY(1000)

        // Refresh chart
        binding.chartActivity.invalidate()
    }

    private fun showActivityRecycleView() {
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

        val adapter = ActivityRecycleViewAdapter(dummyData)
        binding.rvActivity.adapter = adapter
        binding.rvActivity.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.rvActivity.isNestedScrollingEnabled = false
    }

}