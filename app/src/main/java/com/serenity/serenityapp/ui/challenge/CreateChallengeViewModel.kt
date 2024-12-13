package com.serenity.serenityapp.ui.challenge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.serenity.serenityapp.data.model.api.request.ChallengeRequest
import com.serenity.serenityapp.data.model.api.response.RegisterResponse
import com.serenity.serenityapp.data.repository.ChallengeRepository
import com.serenity.serenityapp.pref.SettingPreference
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.HttpException

class CreateChallengeViewModel constructor(
    private val challengeRepository: ChallengeRepository,
    private val settingPreference: SettingPreference
): ViewModel() {
    private val _isCreateSuccess = MutableLiveData(false)
    val isCreateSuccess: LiveData<Boolean> = _isCreateSuccess

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun createChallenge(challengeRequest: ChallengeRequest) {
        _isCreateSuccess.value = false
        _loading.value = true
        _error.value = ""

        viewModelScope.launch {
            try {
                val token = settingPreference.getToken().first()
                val userId = settingPreference.getUserId().first()

                challengeRequest.user_id = userId

                val response = challengeRepository.createChallenge(token, userId, challengeRequest)

                if (response.isSuccess()) {
                    _isCreateSuccess.value = true
                } else {
                    _isCreateSuccess.value = false
                    _error.value = response.message ?: "Internal Server Error"
                }
            } catch (exception: HttpException) {
                val response = exception.response()

                when {
                    response == null -> {
                        _error.value = exception.message
                    }

                    response.errorBody() == null -> {
                        _error.value = exception.message
                    }

                    else -> {
                        val responseBody = Gson().fromJson(response.errorBody()!!.string(), RegisterResponse::class.java)
                        _error.value = responseBody.message ?: "Internal Server Error"
                    }
                }
            } finally {
                _isCreateSuccess.value = false
                _loading.value = false
            }
        }
    }
}