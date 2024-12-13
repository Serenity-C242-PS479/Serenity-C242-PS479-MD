package com.serenity.serenityapp.ui.sentiment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.serenity.serenityapp.data.model.api.response.ChallengeData
import com.serenity.serenityapp.data.model.api.response.RegisterResponse
import com.serenity.serenityapp.data.model.database.UserWithSentiment
import com.serenity.serenityapp.data.repository.ChallengeRepository
import com.serenity.serenityapp.data.repository.NotificationRepository
import com.serenity.serenityapp.pref.SettingPreference
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SentimentViewModel constructor(
    private val notificationRepository: NotificationRepository,
): ViewModel() {
    private val _usersWithSentiments = MutableLiveData<List<UserWithSentiment>>()
    val usersWithSentiments: LiveData<List<UserWithSentiment>> = _usersWithSentiments

    init {
        getUsersWithSentiments()
    }

    private fun getUsersWithSentiments() {
        viewModelScope.launch {
            _usersWithSentiments.value = notificationRepository.getUsersWithSentiments()
        }
    }
}