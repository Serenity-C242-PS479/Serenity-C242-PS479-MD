package com.serenity.serenityapp.helper

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.serenity.serenityapp.data.repository.AuthRepository
import com.serenity.serenityapp.data.repository.ChallengeRepository
import com.serenity.serenityapp.data.repository.NotificationRepository
import com.serenity.serenityapp.data.repository.ProfileRepository
import com.serenity.serenityapp.di.Injection
import com.serenity.serenityapp.pref.SettingPreference
import com.serenity.serenityapp.pref.dataStore
import com.serenity.serenityapp.ui.challenge.ChallengeViewModel
import com.serenity.serenityapp.ui.challenge.CreateChallengeViewModel
import com.serenity.serenityapp.ui.home.HomeViewModel
import com.serenity.serenityapp.ui.login.LoginViewModel
import com.serenity.serenityapp.ui.profile.ProfileViewModel
import com.serenity.serenityapp.ui.register.RegisterViewModel
import com.serenity.serenityapp.ui.sentiment.SentimentViewModel
import com.serenity.serenityapp.ui.setting.SettingViewModel
import com.serenity.serenityapp.ui.splash.SplashViewModel

class ViewModelFactory private constructor(
    private val context: Context,
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val challengeRepository: ChallengeRepository,
    private val notificationRepository: NotificationRepository,
    private val settingPreference: SettingPreference
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SettingViewModel::class.java) -> {
                SettingViewModel(settingPreference) as T
            }

            modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
                SplashViewModel(settingPreference) as T
            }

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(authRepository, settingPreference) as T
            }

            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(authRepository) as T
            }

            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(profileRepository, challengeRepository, settingPreference) as T
            }

            modelClass.isAssignableFrom(ChallengeViewModel::class.java) -> {
                ChallengeViewModel(challengeRepository, settingPreference) as T
            }

            modelClass.isAssignableFrom(CreateChallengeViewModel::class.java) -> {
                CreateChallengeViewModel(challengeRepository, settingPreference) as T
            }

            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(profileRepository, settingPreference) as T
            }

            modelClass.isAssignableFrom(SentimentViewModel::class.java) -> {
                SentimentViewModel(notificationRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    context,
                    Injection.provideAuthRepository(),
                    Injection.provideProfileRepository(),
                    Injection.provideChallengeRepository(),
                    Injection.provideNotificationRepository(context.applicationContext),
                    SettingPreference.getInstance(context.applicationContext.dataStore),
                )
            }.also { instance = it }
    }
}