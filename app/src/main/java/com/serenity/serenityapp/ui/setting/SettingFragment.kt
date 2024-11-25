package com.serenity.serenityapp.ui.setting

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
import com.serenity.serenityapp.data.model.AppLimit
import com.serenity.serenityapp.databinding.FragmentSettingBinding
import com.serenity.serenityapp.databinding.FragmentUsageBinding
import com.serenity.serenityapp.ui.adapter.ActivityRecycleViewAdapter
import com.serenity.serenityapp.ui.adapter.AppLimtRecycleViewAdapter

class SettingFragment: Fragment(R.layout.fragment_setting) {
    private lateinit var binding: FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(inflater, container, false)

        showAppLimitRecycleView()

        return binding.root
    }

    private fun showAppLimitRecycleView() {
        val dummyData = listOf(
            AppLimit(
                appId = "com.whatsapp",
                appName = "WhatsApp",
                hour = "02:00"
            ),
            AppLimit(
                appId = "com.instagram.android",
                appName = "Instagram",
                hour = "01:00"
            ),
            AppLimit(
                appId = "com.youtube.android",
                appName = "YouTube",
                hour = "03:00"
            ),
            AppLimit(
                appId = "com.facebook.katana",
                appName = "Facebook",
                hour = "01:20"
            )
        )

        val adapter = AppLimtRecycleViewAdapter(dummyData)
        binding.rvAppLimit.adapter = adapter
        binding.rvAppLimit.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.rvAppLimit.isNestedScrollingEnabled = false
    }
}