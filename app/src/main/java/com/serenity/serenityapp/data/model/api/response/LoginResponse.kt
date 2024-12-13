package com.serenity.serenityapp.data.model.api.response

data class LoginResponse (
    val status: String?,
    val message: String?,
    val data: LoginData,
    val accessToken: String,
    val refreshToken: String
) {
    fun isSuccess(): Boolean {
        return status != null && status == "success"
    }
}

data class LoginData(
    val user_id: Int,
    val name: String,
    val email: String,
    val password: String,
    val age: Int,
    val gender: String,
    val photo_profile: String,
)