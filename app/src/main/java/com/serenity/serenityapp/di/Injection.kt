package com.serenity.serenityapp.di

import android.app.Application
import android.content.Context
import com.serenity.serenityapp.data.api.ApiBackendConfig
import com.serenity.serenityapp.data.api.ApiPredictBreakReminderConfig
import com.serenity.serenityapp.data.api.ApiPredictBreakReminderService
import com.serenity.serenityapp.data.api.ApiPredictSentimentConfig
import com.serenity.serenityapp.data.database.SerenityRoom
import com.serenity.serenityapp.data.repository.AuthRepository
import com.serenity.serenityapp.data.repository.ChallengeRepository
import com.serenity.serenityapp.data.repository.NotificationRepository
import com.serenity.serenityapp.data.repository.PredictionRepository
import com.serenity.serenityapp.data.repository.ProfileRepository

object Injection {
    fun provideAuthRepository(): AuthRepository {
        val apiService = ApiBackendConfig.getApiService()
        return AuthRepository.getInstance(apiService)
    }

    fun provideProfileRepository(): ProfileRepository {
        val apiService = ApiBackendConfig.getApiService()
        return ProfileRepository.getInstance(apiService)
    }

    fun provideChallengeRepository(): ChallengeRepository {
        val apiService = ApiBackendConfig.getApiService()
        return ChallengeRepository.getInstance(apiService)
    }

    fun providePredictionRepository(): PredictionRepository {
        val apiPredictBreakReminderService = ApiPredictBreakReminderConfig.getApiService()
        val apiPredictSentimentService = ApiPredictSentimentConfig.getApiService()
        return PredictionRepository.getInstance(
            apiPredictBreakReminderService,
            apiPredictSentimentService
        )
    }

    fun provideNotificationRepository(context: Context): NotificationRepository {
        val db = SerenityRoom.getDatabase(context)
        val userDao = db.userDao()
        val chatDao = db.chatDao()
        val sentimentDao = db.sentimentDao()
        return NotificationRepository.getInstance(
            userDao,
            chatDao,
            sentimentDao
        )
    }
}