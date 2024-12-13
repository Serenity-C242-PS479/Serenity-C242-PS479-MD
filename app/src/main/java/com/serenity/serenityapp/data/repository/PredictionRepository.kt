package com.serenity.serenityapp.data.repository

import com.serenity.serenityapp.data.api.ApiBackendService
import com.serenity.serenityapp.data.api.ApiPredictBreakReminderService
import com.serenity.serenityapp.data.api.ApiPredictSentimentService
import com.serenity.serenityapp.data.model.api.response.PredictBreakReminderResponse
import com.serenity.serenityapp.data.model.api.response.PredictSentimentResponse
import com.serenity.serenityapp.data.model.api.response.ProfileData
import com.serenity.serenityapp.data.model.api.response.ProfileResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class PredictionRepository private constructor(
    private val apiPredictBreakReminderService: ApiPredictBreakReminderService,
    private val apiPredictSentimentService: ApiPredictSentimentService,
) {
    suspend fun predictBreakReminder(features: Map<String, Int>): PredictBreakReminderResponse {
        return withContext(Dispatchers.IO) {
            apiPredictBreakReminderService.predict(features)
        }
    }

    suspend fun predictSentiment(features: Map<String, String>): PredictSentimentResponse {
        return withContext(Dispatchers.IO) {
            apiPredictSentimentService.predict(features)
        }
    }

    companion object {
        @Volatile
        private var instance: PredictionRepository? = null
        fun getInstance(
            apiPredictBreakReminderService: ApiPredictBreakReminderService,
            apiPredictSentimentService: ApiPredictSentimentService,
        ): PredictionRepository =
            instance ?: synchronized(this) {
                instance ?: PredictionRepository(
                    apiPredictBreakReminderService,
                    apiPredictSentimentService
                )
            }.also { instance = it }
    }
}