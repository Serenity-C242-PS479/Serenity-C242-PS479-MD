package com.serenity.serenityapp.data.model.api.response

import java.time.ZonedDateTime

data class RegisterResponse(
    val status: String?,
    val message: String?,
    val data: RegisterData
) {
    fun isSuccess(): Boolean {
        return status != null && status == "success"
    }
}

data class RegisterData(
    val user_id: Int,
    val name: String,
    val email: String,
    val password: String,
    val age: String,
    val gender: String,
    val photo_profile: String,
)
