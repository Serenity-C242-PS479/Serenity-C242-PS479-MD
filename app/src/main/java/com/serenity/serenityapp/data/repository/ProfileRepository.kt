package com.serenity.serenityapp.data.repository

import com.serenity.serenityapp.data.api.ApiBackendService
import com.serenity.serenityapp.data.model.api.response.ProfileData
import com.serenity.serenityapp.data.model.api.response.ProfileResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ProfileRepository private constructor(private val apiBackendService: ApiBackendService) {
    suspend fun getProfile(token: String, userId: Int): ProfileResponse {
        return withContext(Dispatchers.IO) {
            apiBackendService.getProfile(token, userId)
        }
    }

    suspend fun editProfile(token: String, userId: Int, profile: ProfileData, photoFile: File?): ProfileResponse {
        return withContext(Dispatchers.IO) {
            val name = profile.name.toRequestBody("text/plain".toMediaTypeOrNull())
            val age = profile.age.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val gender = profile.gender.toRequestBody("text/plain".toMediaTypeOrNull())
            val email = profile.email.toRequestBody("text/plain".toMediaTypeOrNull())

            var password: RequestBody? = null
            if (!profile.password.isNullOrEmpty()) {
                password = profile.password.toRequestBody("text/plain".toMediaTypeOrNull())
            }

            var photo: MultipartBody.Part? = null;
            if (photoFile != null) {
                val requestFile = photoFile.asRequestBody("image/*".toMediaTypeOrNull())
                photo = MultipartBody.Part.createFormData("photo", photoFile.name, requestFile)
            }

            apiBackendService.editProfile(token, userId, name, age, gender, email, password, photo)
        }
    }

    companion object {
        @Volatile
        private var instance: ProfileRepository? = null
        fun getInstance(
            apiBackendService: ApiBackendService
        ): ProfileRepository =
            instance ?: synchronized(this) {
                instance ?: ProfileRepository(apiBackendService)
            }.also { instance = it }
    }
}