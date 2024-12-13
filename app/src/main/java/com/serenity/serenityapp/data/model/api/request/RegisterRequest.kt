package com.serenity.serenityapp.data.model.api.request

data class RegisterRequest (
    val name: String,
    val email: String,
    val password: String,
    val age: Int,
    val gender: String,
)