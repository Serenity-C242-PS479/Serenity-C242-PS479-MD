package com.serenity.serenityapp.data.repository

import com.serenity.serenityapp.data.api.ApiBackendService
import com.serenity.serenityapp.data.model.api.request.LoginRequest
import com.serenity.serenityapp.data.model.api.request.RefreshTokenRequest
import com.serenity.serenityapp.data.model.api.request.RegisterRequest
import com.serenity.serenityapp.data.model.api.response.LoginResponse
import com.serenity.serenityapp.data.model.api.response.RefreshTokenResponse
import com.serenity.serenityapp.data.model.api.response.RegisterResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository private constructor(private val apiBackendService: ApiBackendService) {
    suspend fun login(loginRequest: LoginRequest): LoginResponse {
        return withContext(Dispatchers.IO) {
            apiBackendService.login(loginRequest)
        }
    }

    suspend fun register(registerRequest: RegisterRequest): RegisterResponse {
        return withContext(Dispatchers.IO) {
            apiBackendService.register(registerRequest)
        }
    }

    suspend fun refreshToken(refreshTokenRequest: RefreshTokenRequest): RefreshTokenResponse {
        return withContext(Dispatchers.IO) {
            apiBackendService.refreshToken(refreshTokenRequest)
        }
    }

    companion object {
        @Volatile
        private var instance: AuthRepository? = null
        fun getInstance(
            apiBackendService: ApiBackendService
        ): AuthRepository =
            instance ?: synchronized(this) {
                instance ?: AuthRepository(apiBackendService)
            }.also { instance = it }
    }
}