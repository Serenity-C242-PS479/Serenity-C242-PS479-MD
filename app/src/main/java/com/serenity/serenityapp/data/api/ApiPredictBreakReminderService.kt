package com.serenity.serenityapp.data.api

import com.serenity.serenityapp.data.model.api.response.LoginResponse
import com.serenity.serenityapp.data.model.api.response.PredictBreakReminderResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiPredictBreakReminderService {
    @POST("serenity-break-reminder-function")
    suspend fun predict(
        @Body features: Map<String, Int>
    ): PredictBreakReminderResponse
}