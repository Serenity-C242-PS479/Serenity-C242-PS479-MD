package com.serenity.serenityapp.helper

import android.accessibilityservice.AccessibilityService
import android.app.usage.UsageStatsManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import com.serenity.serenityapp.data.AppData
import com.serenity.serenityapp.data.model.AppStat
import com.serenity.serenityapp.data.model.api.response.ProfileData
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.concurrent.TimeUnit

enum class StartTime {
    TODAY,
    WEEK,
    MONTH
}

fun getFormatedTimeUsed(totalTimeUsed: Long): String {
    val hours = totalTimeUsed / 3600
    val minutes = (totalTimeUsed % 3600) / 60
    val seconds = totalTimeUsed % 60

    return when {
        hours > 0 && minutes > 0 && seconds > 0 -> "${hours}h ${minutes}m ${seconds}s"
        hours > 0 && minutes > 0 -> "${hours}h ${minutes}m"
        hours > 0 -> "${hours}h"
        minutes > 0 && seconds > 0 -> "${minutes}m ${seconds}s"
        minutes > 0 -> "${minutes}m"
        else -> "${seconds}s"
    }
}

fun Context.getOnScreenAppPackage(): String{
    requestUsageStatsPermission()

    val usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

    val currentTime = System.currentTimeMillis()

    val usageStatsList = usageStatsManager.queryUsageStats(
        UsageStatsManager.INTERVAL_DAILY,
        currentTime - 1000 * 60,
        currentTime
    )

    val sortedList = usageStatsList.sortedByDescending { it.lastTimeUsed }

    if (sortedList.isEmpty()) return ""

    return sortedList[0].packageName
}

fun Context.getSocialMediaUsages(startTimeParam: StartTime, size: Int? = null): List<AppStat> {
    requestUsageStatsPermission()

    var appStats = ArrayList<AppStat>()

    val usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    val calendar = Calendar.getInstance().apply {
        if (startTimeParam == StartTime.WEEK) {
            set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        }

        if (startTimeParam == StartTime.MONTH) {
            set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        }

        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 1)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    val startTime = calendar.timeInMillis
    val endTime = System.currentTimeMillis()

    val usageStatsList = usageStatsManager.queryUsageStats(
        UsageStatsManager.INTERVAL_DAILY,
        startTime,
        endTime
    )

    if (usageStatsList.isNullOrEmpty()) {
        return listOf()
    }

    for (usageStats in usageStatsList) {
        val packageName = usageStats.packageName
        val firstTimeStamp = usageStats.firstTimeStamp
        val lastTimeStamp = usageStats.lastTimeStamp
        val lastTimeUsed = usageStats.lastTimeUsed
        val totalTimeInForeground = usageStats.totalTimeInForeground / 1000

        if (!AppData.appCodeList.contains(packageName)) continue

        val existingAppStat = appStats.find { it.packageName == packageName }

        if (existingAppStat != null) {
            val index = appStats.indexOf(existingAppStat)
            appStats[index].updateLastTimestamp(lastTimeStamp)
            appStats[index].updateLastTimeUsed(lastTimeUsed)
            appStats[index].updateTotalTimeUsed(totalTimeInForeground)
        } else {
            val appStat = AppStat(
                packageName,
                AppData.appNameMap[packageName]!!,
                firstTimeStamp,
                lastTimeStamp,
                lastTimeUsed,
                totalTimeInForeground,
                AppData.appCardBackgroundMap[packageName]!!,
                AppData.appTextColorMap[packageName]!!,
            )

            appStats.add(appStat)
        }
    }

    val sortedList = appStats.sortedByDescending { it.totalTimeUsed }

    return size?.let { sortedList.take(it) } ?: sortedList
}

fun Context.buildPredictBreakReminderFeatures(profile: ProfileData?): Map<String, Int> {
    val age = profile?.age ?: 0;

    val features = mutableMapOf(
        "Age" to age,
        "Facebook" to 0,
        "Instagram" to 0,
        "Reddit" to 0,
        "Threads" to 0,
        "TikTok" to 0,
        "X" to 0,
        "YouTube" to 0,
        "00:00:00" to 0,
        "01:00:00" to 0,
        "02:00:00" to 0,
        "03:00:00" to 0,
        "04:00:00" to 0,
        "05:00:00" to 0,
        "06:00:00" to 0,
        "07:00:00" to 0,
        "08:00:00" to 0,
        "09:00:00" to 0,
        "10:00:00" to 0,
        "11:00:00" to 0,
        "12:00:00" to 0,
        "13:00:00" to 0,
        "14:00:00" to 0,
        "15:00:00" to 0,
        "16:00:00" to 0,
        "17:00:00" to 0,
        "18:00:00" to 0,
        "19:00:00" to 0,
        "20:00:00" to 0,
        "21:00:00" to 0,
        "22:00:00" to 0,
        "23:00:00" to 0
    )

    val usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 1)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    val startTime = calendar.timeInMillis
    val endTime = System.currentTimeMillis()

    val usageStatsList = usageStatsManager.queryUsageStats(
        UsageStatsManager.INTERVAL_DAILY,
        startTime,
        endTime
    )

    for (usageStats in usageStatsList) {
        val packageName = usageStats.packageName
        val firstTimeStamp = usageStats.firstTimeStamp
        val lastTimeStamp = usageStats.lastTimeStamp

        if (!AppData.appCodeList.contains(packageName)) continue

        for ((appCode, appName) in AppData.appNameMap) {
            if (appCode == packageName) {
                features[appName] = 1
            }
        }

        for (hour in 0..23) {
            val time = String.format("%02d:00:00", hour)
            val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
            val localTime = LocalTime.parse(time, timeFormatter)

            val today = LocalDate.now()
            val localDateTime = today.atTime(localTime)
            val millisecondLocalTime = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

            if (millisecondLocalTime in (firstTimeStamp + 1)..<lastTimeStamp) {
                features[time] = 1
            }
        }
    }

    return features
}

fun Context.getHourlySocialMediaUsages(startTimeParam: StartTime, size: Int? = null): Map<String, List<AppStat>> {
    requestUsageStatsPermission()

    var hourlyAppStats = mutableMapOf<String, ArrayList<AppStat>>()
    var appStats = ArrayList<AppStat>()

    val usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    val calendar = Calendar.getInstance().apply {
        if (startTimeParam == StartTime.WEEK) {
            set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        }

        if (startTimeParam == StartTime.MONTH) {
            set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        }

        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 1)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    val startTime = calendar.timeInMillis
    val endTime = System.currentTimeMillis()

    val usageStatsList = usageStatsManager.queryUsageStats(
        UsageStatsManager.INTERVAL_DAILY,
        startTime,
        endTime
    )

    if (usageStatsList.isNullOrEmpty()) {
        return hourlyAppStats
    }

    for (usageStats in usageStatsList) {
        val packageName = usageStats.packageName
        val firstTimeStamp = usageStats.firstTimeStamp
        val lastTimeStamp = usageStats.lastTimeStamp
        val lastTimeUsed = usageStats.lastTimeUsed
        val totalTimeInForeground = usageStats.totalTimeInForeground / 1000

        if (!AppData.appCodeList.contains(packageName)) continue

        val appStat = AppStat(
            packageName,
            AppData.appNameMap[packageName]!!,
            firstTimeStamp,
            lastTimeStamp,
            lastTimeUsed,
            totalTimeInForeground,
            AppData.appCardBackgroundMap[packageName]!!,
            AppData.appTextColorMap[packageName]!!,
        )

        appStats.add(appStat)
    }

    for (hour in 0..23) {
        val hourLabel = String.format("%02d:00", hour)

        val statsForThisHour = appStats.filter { appStat ->
            val appFirstTime = TimeUnit.MILLISECONDS.toHours(appStat.startTimestamp) % 24
            val appLastTime = TimeUnit.MILLISECONDS.toHours(appStat.lastTimeUsed) % 24

            (hour in appFirstTime..appLastTime)
        }

        // Tambahkan data ke map jika ada
        if (statsForThisHour.isNotEmpty()) {
            if (statsForThisHour.any { it.packageName == AppData.REDDIT_NAME}) {
                Log.d("TESTING", "getHourlySocialMediaUsages: " + hourLabel)
                Log.d("TESTING", "getHourlySocialMediaUsages: " + statsForThisHour)
            }

            hourlyAppStats[hourLabel] = ArrayList(statsForThisHour)
        }
    }

    return hourlyAppStats
}

fun Context.requestUsageStatsPermission() {
    if (!checkUsageStatsPermission()) {
        startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
    }
}

fun Context.checkUsageStatsPermission(): Boolean {
    val appOps = getSystemService(Context.APP_OPS_SERVICE) as android.app.AppOpsManager
    val mode = appOps.checkOpNoThrow(
        android.app.AppOpsManager.OPSTR_GET_USAGE_STATS,
        android.os.Process.myUid(),
        packageName
    )
    return mode == android.app.AppOpsManager.MODE_ALLOWED
}

fun Context.checkAccessbility(serviceClass: Class<out AccessibilityService>): Boolean {
    val expectedComponentName = ComponentName(this, serviceClass)
    val enabledServices = Settings.Secure.getString(
        this.contentResolver,
        Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
    ) ?: return false
    val isEnabled = enabledServices.split(":")
        .any { it.equals(expectedComponentName.flattenToString(), ignoreCase = true) }
    val accessibilityEnabled = Settings.Secure.getInt(
        this.contentResolver,
        Settings.Secure.ACCESSIBILITY_ENABLED, 0
    ) == 1
    return isEnabled && accessibilityEnabled
}
