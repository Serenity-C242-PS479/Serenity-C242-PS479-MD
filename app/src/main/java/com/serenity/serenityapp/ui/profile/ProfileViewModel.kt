package com.serenity.serenityapp.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.serenity.serenityapp.data.model.api.response.ProfileData
import com.serenity.serenityapp.data.model.api.response.RegisterResponse
import com.serenity.serenityapp.data.repository.ProfileRepository
import com.serenity.serenityapp.pref.SettingPreference
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.File

class ProfileViewModel constructor(
    private val profileRepository: ProfileRepository,
    private val settingPreference: SettingPreference
): ViewModel() {
    private val _profile = MutableLiveData<ProfileData>()
    val profile: LiveData<ProfileData> = _profile

    private val _isEditSuccess = MutableLiveData(false)
    val isEditSuccess: LiveData<Boolean> = _isEditSuccess

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        getProfile()
    }

    private fun getProfile() {
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

    fun editProfile(profileData: ProfileData, photo: File?) {
        _isEditSuccess.value = false
        _loading.value = true
        _error.value = ""

        viewModelScope.launch {
            try {
                val token = settingPreference.getToken().first()
                val userId = settingPreference.getUserId().first()

                val response = profileRepository.editProfile(token, userId, profileData, photo)

                if (response.isSuccess()) {
                    _isEditSuccess.value = true
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
}