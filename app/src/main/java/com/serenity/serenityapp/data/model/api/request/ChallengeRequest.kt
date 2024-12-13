package com.serenity.serenityapp.data.model.api.request

data class ChallengeRequest (
    var user_id: Int?,
    val title: String,
    val start_hour: String,
    val end_hour: String,
    var status: String,
)