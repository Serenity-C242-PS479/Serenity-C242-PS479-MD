package com.serenity.serenityapp.data.model.api.response

import java.time.ZonedDateTime

data class PredictSentimentResponse (
    val sentiment: String
) {
    fun isPositive(): Boolean {
        return sentiment == "Positive"
    }

    fun isNeutral(): Boolean {
        return sentiment == "Neutral"
    }

    fun isNegative(): Boolean {
        return sentiment == "Negative"
    }
}