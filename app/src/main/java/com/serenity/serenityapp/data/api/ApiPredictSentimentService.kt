package com.serenity.serenityapp.data.api

import com.serenity.serenityapp.data.model.api.response.LoginResponse
import com.serenity.serenityapp.data.model.api.response.PredictSentimentResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiPredictSentimentService {
    @POST("serenity-sentiment-function")
    suspend fun predict(
        @Body features: Map<String, String>
    ): PredictSentimentResponse
}