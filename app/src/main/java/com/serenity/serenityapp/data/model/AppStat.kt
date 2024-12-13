package com.serenity.serenityapp.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.concurrent.TimeUnit

data class AppStat(
    val packageName: String,
    val appName: String,
    val startTimestamp: Long,
    var lastTimestamp: Long,
    var lastTimeUsed: Long,
    var totalTimeUsed: Long,
    val cardBackgroundColor: String,
    val textColor: String,
) {
    fun updateLastTimestamp(lastTimestamp: Long) {
        if (lastTimestamp > this.lastTimestamp) {
            this.lastTimestamp = lastTimestamp
        }
    }

    fun updateLastTimeUsed(lastTimeUsed: Long) {
        if (lastTimeUsed > this.lastTimeUsed) {
            this.lastTimeUsed = lastTimeUsed
        }
    }

    fun updateTotalTimeUsed(totalTimeUsed: Long) {
        this.totalTimeUsed += totalTimeUsed
    }

    fun getFormatedLastTimeUsed(): String {
        val currentTime = System.currentTimeMillis()
        val diffInMillis = currentTime - lastTimeUsed

        // Konversi perbedaan waktu ke hari, jam, menit, dan detik
        val days = TimeUnit.MILLISECONDS.toDays(diffInMillis)
        val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(diffInMillis) % 60

        return when {
            days > 0 -> "Last used ${days} day${if (days > 1) "s" else ""} ago"
            hours > 0 -> "Last used ${hours} hour${if (hours > 1) "s" else ""} ago"
            minutes > 0 -> "Last used ${minutes} minute${if (minutes > 1) "s" else ""} ago"
            seconds > 0 -> "Last used ${seconds} second${if (seconds > 1) "s" else ""} ago"
            else -> "Just now"
        }
    }

    fun getFormatedTimeUsed(): String {
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
}
