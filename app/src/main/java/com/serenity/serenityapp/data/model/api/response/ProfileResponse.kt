package com.serenity.serenityapp.data.model.api.response

data class ProfileResponse (
    val status: String?,
    val message: String?,
    val data: ProfileData,
) {
    fun isSuccess(): Boolean {
        return status != null && status == "success"
    }
}

data class ProfileData(
    val user_id: Int?,
    val name: String,
    val email: String,
    val password: String? = null,
    val age: Int,
    val gender: String,
    val photo_profile: String?,
)