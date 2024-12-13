package com.serenity.serenityapp.data.model.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.concurrent.TimeUnit

@Entity(tableName = "sentiments")
@Parcelize
data class Sentiment(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int?,

    @ColumnInfo(name = "user_id")
    val userId: Int,

    @ColumnInfo(name = "sentiment")
    val sentiment: String,

    @ColumnInfo(name = "retrieve_at")
    val predictAt: Long,
): Parcelable {
    fun predictAtText(): String {
        val currentTime = System.currentTimeMillis()
        val diffInMillis = currentTime - predictAt

        val days = TimeUnit.MILLISECONDS.toDays(diffInMillis)
        val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(diffInMillis) % 60

        return when {
            days > 0 -> "Sentiment for ${days} day${if (days > 1) "s" else ""} ago"
            hours > 0 -> "Sentiment for ${hours} hour${if (hours > 1) "s" else ""} ago"
            minutes > 0 -> "Sentiment for ${minutes} minute${if (minutes > 1) "s" else ""} ago"
            seconds > 0 -> "Sentiment for ${seconds} second${if (seconds > 1) "s" else ""} ago"
            else -> "Sentiment for just now"
        }
    }
}