package com.serenity.serenityapp.data.model.api.response

import java.time.ZonedDateTime

data class PredictBreakReminderResponse (
    val status: String
) {
    fun isNoNeedBreak(): Boolean {
        return status == "tidak perlu istirahat"
    }
}