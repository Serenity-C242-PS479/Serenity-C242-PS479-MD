package com.serenity.serenityapp.data.model.api.response

import java.time.ZonedDateTime

data class RefreshTokenResponse (
    val accessToken: String,
    val status: String?,
    val message: String?,
) {
    fun isSuccess(): Boolean {
        return status != null && status == "success"
    }
}