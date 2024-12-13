package com.serenity.serenityapp.ui.usage

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Visibility
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.data.ScatterData
import com.github.mikephil.charting.data.ScatterDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.serenity.serenityapp.R
import com.serenity.serenityapp.data.AppData
import com.serenity.serenityapp.data.model.Activity
import com.serenity.serenityapp.data.model.AppStat
import com.serenity.serenityapp.data.model.Challenge
import com.serenity.serenityapp.databinding.FragmentHomeBinding
import com.serenity.serenityapp.databinding.FragmentUsageBinding
import com.serenity.serenityapp.helper.StartTime
import com.serenity.serenityapp.helper.getFormatedTimeUsed
import com.serenity.serenityapp.helper.getHourlySocialMediaUsages
import com.serenity.serenityapp.helper.getSocialMediaUsages
import com.serenity.serenityapp.ui.adapter.ActivityRecentRecycleViewAdapter
import com.serenity.serenityapp.ui.adapter.ActivityRecycleViewAdapter
import com.serenity.serenityapp.ui.adapter.ChallengeRecycleViewAdapter
import kotlin.math.log

class UsageFragment: Fragment(R.layout.fragment_usage) {
    private lateinit var binding: FragmentUsageBinding
    private lateinit var activities: List<AppStat>
    private lateinit var hourlyActivities: Map<String, List<AppStat>>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUsageBinding.inflate(inflater, container, false)

        fetchSocialMediaUsages(StartTime.TODAY)
        showActivityRecycleView()
        setupChangeListener()
        setupChartNavigation()

        return binding.root
    }

    private fun setupChangeListener() {
        binding.spnFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parentView.getItemAtPosition(position).toString()

                when(selectedItem) {
                    "Today" -> {
                        fetchSocialMediaUsages(StartTime.TODAY)
                        showActivityRecycleView()
                    }

                    "This Week" -> {
                        fetchSocialMediaUsages(StartTime.WEEK)
                        showActivityRecycleView()
                    }

                    "This Month" -> {
                        fetchSocialMediaUsages(StartTime.MONTH)
                        showActivityRecycleView()
                    }
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {}

        }
    }

    private fun fetchSocialMediaUsages(startTime: StartTime) {
        activities = requireActivity().getSocialMediaUsages(startTime)
        hourlyActivities = requireActivity().getHourlySocialMediaUsages(startTime)

        showActivityPieChart()
        showActivityBarChart()
        showActivityScatterChart()
    }

    private fun showActivityPieChart() {
        val sumTotalTimeUsed = activities.sumOf { it.totalTimeUsed }

        // PieChart Datas
        val entries = ArrayList<PieEntry>()
        val colors = ArrayList<Int>()

        for (activity in activities) {
            val percentage = (activity.totalTimeUsed.toFloat() / sumTotalTimeUsed.toFloat()) * 100
            entries.add(PieEntry(percentage, activity.appName))
            colors.add(Color.parseColor(activity.cardBackgroundColor))
        }

        // Dataset
        val pieDataSet = PieDataSet(entries, "Categories")
        pieDataSet.colors = colors
        pieDataSet.valueTextColor = Color.WHITE
        pieDataSet.valueTextSize = 12f

        // Apply Dataset to PieData
        val pieData = PieData(pieDataSet)
        binding.chartActivityPie.data = pieData

        // Setting Chart
        binding.chartActivityPie.description.isEnabled = false
        binding.chartActivityPie.setCenterTextSize(16f)
        binding.chartActivityPie.legend.isEnabled = false
        binding.chartActivityPie.animateY(1000)

        // Refresh chart
        binding.chartActivityPie.invalidate()
    }

    private fun showActivityBarChart() {
        val sumTotalTimeUsed = activities.sumOf { it.totalTimeUsed }

        // BarChart Data
        val entries = ArrayList<BarEntry>()
        val colors = ArrayList<Int>()

        // Create BarEntry for each activity
        for ((index, activity) in activities.withIndex()) {
            val percentage = (activity.totalTimeUsed.toFloat() / sumTotalTimeUsed.toFloat()) * 100
            entries.add(BarEntry(index.toFloat(), percentage)) // Use index for X-axis
            colors.add(Color.parseColor(activity.cardBackgroundColor))
        }

        // Create a BarDataSet to hold the entries
        val barDataSet = BarDataSet(entries, "Categories")
        barDataSet.colors = colors
        barDataSet.valueTextColor = Color.WHITE
        barDataSet.valueTextSize = 12f

        // Apply the BarDataSet to BarData
        val barData = BarData(barDataSet)
        binding.chartActivityBar.data = barData

        // Set the X-axis labels if you want custom labels
        val xAxis = binding.chartActivityBar.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(activities.map { it.appName })

        // Customize the BarChart
        binding.chartActivityBar.description.isEnabled = false
        binding.chartActivityBar.setFitBars(true) // Fit the bars to the chart
        binding.chartActivityBar.animateY(1000) // Animate the chart

        // Disable legend if not needed
        binding.chartActivityBar.legend.isEnabled = false

        // Refresh chart
        binding.chartActivityBar.invalidate()
    }

    private fun showActivityScatterChart() {
        // ScatterChart Data
        val entries = ArrayList<Entry>()
        val colors = ArrayList<Int>()

        for ((hour, activities) in hourlyActivities) {
            val hourString = hour.split(":")[0]

            for (activity in activities) {
                val index = AppData.appCodeList.indexOf(activity.packageName)
                entries.add(Entry(hourString.toFloat(), index.toFloat()))

                if (activity.packageName == AppData.X_TWITTER_CODE) {
                    Log.d("UsageFragment", "showActivityScatterChart: " + activity.appName)
                    Log.d("UsageFragment", "showActivityScatterChart: " + activity.cardBackgroundColor)
                }
            }
        }

        for (appCode in AppData.appCodeList) {
            val backgroundColor = Color.parseColor(AppData.appCardBackgroundMap[appCode])
            colors.add(backgroundColor)
        }

        // Create a ScatterDataSet to hold the entries
        val scatterDataSet = ScatterDataSet(entries, "Categories")
        scatterDataSet.colors = colors
        scatterDataSet.valueTextColor = Color.TRANSPARENT
        scatterDataSet.valueTextSize = 12f

        // Apply the ScatterDataSet to ScatterData
        val scatterData = ScatterData(scatterDataSet)
        binding.chartActivityScatter.data = scatterData

        // Set X-axis (hours)
        val xAxis = binding.chartActivityScatter.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter((0..23).map { String.format("%02d:00", it)  })
        xAxis.granularity = 1f // Ensure labels are displayed for each hour
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.axisMinimum = -1f // Start at 1
        xAxis.axisMaximum = 24f // End at 24
        xAxis.labelCount = 24

        // Set Y-axis (application names)
        val yAxisLeft = binding.chartActivityScatter.axisLeft
        yAxisLeft.valueFormatter = IndexAxisValueFormatter(AppData.appCodeList.map { AppData.appNameMap[it] })
        yAxisLeft.granularity = 1f // Ensure labels match entries

        // Hide right Y-axis (optional)
        binding.chartActivityScatter.axisRight.isEnabled = false

        // Customize the ScatterChart
        binding.chartActivityScatter.description.isEnabled = false
        binding.chartActivityScatter.setDrawGridBackground(false) // Optional: Hide grid background
        binding.chartActivityScatter.animateY(1000) // Animate the chart

        // Disable legend if not needed
        binding.chartActivityScatter.legend.isEnabled = false

        // Refresh chart
        binding.chartActivityScatter.invalidate()
    }

    private fun showActivityRecycleView() {
        val adapter = ActivityRecycleViewAdapter(activities)
        val totalTimeUsed = activities.sumOf { it.totalTimeUsed }

        binding.rvActivity.adapter = adapter
        binding.rvActivity.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.rvActivity.isNestedScrollingEnabled = false
        binding.tvTotalUsedTime.text = "Total Used Time: ${getFormatedTimeUsed(totalTimeUsed)}"
    }

    private fun setupChartNavigation() {
        binding.bnvChart.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.navigation_chart_pie -> {
                    binding.chartActivityBar.visibility = View.GONE
                    binding.chartActivityPie.visibility = View.VISIBLE
                    binding.chartActivityScatter.visibility = View.GONE
                    true
                }

                R.id.navigation_chart_bar -> {
                    binding.chartActivityBar.visibility = View.VISIBLE
                    binding.chartActivityPie.visibility = View.GONE
                    binding.chartActivityScatter.visibility = View.GONE
                    true
                }

                R.id.navigation_chart_scatter -> {
                    binding.chartActivityBar.visibility = View.GONE
                    binding.chartActivityPie.visibility = View.GONE
                    binding.chartActivityScatter.visibility = View.VISIBLE
                    true
                }

                else -> false
            }
        }
    }
}