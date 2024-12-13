package com.serenity.serenityapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.serenity.serenityapp.data.model.api.response.ChallengeData
import com.serenity.serenityapp.data.model.api.response.ProfileData
import com.serenity.serenityapp.data.model.api.response.RegisterResponse
import com.serenity.serenityapp.data.repository.ChallengeRepository
import com.serenity.serenityapp.data.repository.ProfileRepository
import com.serenity.serenityapp.pref.SettingPreference
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeViewModel constructor(
    private val profileRepository: ProfileRepository,
    private val challengeRepository: ChallengeRepository,
    private val settingPreference: SettingPreference
): ViewModel() {
    private val _challenges = MutableLiveData<List<ChallengeData>>()
    val challenges: LiveData<List<ChallengeData>> = _challenges

    private val _profile = MutableLiveData<ProfileData>()
    val profile: LiveData<ProfileData> = _profile

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getProfile() {
        _loading.value = true
        _error.value = ""

        viewModelScope.launch {
            try {
                val token = settingPreference.getToken().first()
                val userId = settingPreference.getUserId().first()

                val response = profileRepository.getProfile(token, userId)

                if (response.isSuccess()) {
                    _profile.value = response.data
                } else {
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
                _loading.value = false
            }
        }
    }

    fun getChallenges() {
        _loading.value = true
        _error.value = ""

        viewModelScope.launch {
            try {
                val token = settingPreference.getToken().first()
                val userId = settingPreference.getUserId().first()

                val response = challengeRepository.getChallenge(token, userId)

                if (response.isSuccess()) {
                    _challenges.value = response.datas
                } else {
                    _error.value = response.message ?: "Internal Server Error"
                }
            } catch (exception: HttpException) {
                _challenges.value = listOf()
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
                _loading.value = false
            }
        }
    }
}