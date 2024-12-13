package com.serenity.serenityapp.data.model.api.response

import java.time.Duration
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

data class ChallengeResponse (
    val status: String?,
    val message: String?,
    val datas: List<ChallengeData>
) {
    fun isSuccess(): Boolean {
        return status != null && status == "success"
    }
}

data class ChallengeData(
    val id: Int,
    val user_id: Int,
    val title: String,
    val start_hour: String,
    val end_hour: String,
    val status: String,
    val createdAt: String,
    val updatedAt: String,
) {
    fun createdAtText(): String {
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val createdTime = ZonedDateTime.parse(createdAt, formatter)
        val now = ZonedDateTime.now()

        val duration = Duration.between(createdTime, now)

        return when {
            duration.toDays() > 0 -> "Created at ${duration.toDays()} day(s) ago"
            duration.toHours() > 0 -> "Created at ${duration.toHours()} hour(s) ago"
            duration.toMinutes() > 0 -> "Created at ${duration.toMinutes()} minute(s) ago"
            else -> "Created just now"
        }
    }

    fun startHour(): String {
        return start_hour.substring(0, 5)
    }

    fun endHour(): String {
        return end_hour.substring(0, 5)
    }
}