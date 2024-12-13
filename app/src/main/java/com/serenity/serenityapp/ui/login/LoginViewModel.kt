package com.serenity.serenityapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.serenity.serenityapp.data.model.api.request.LoginRequest
import com.serenity.serenityapp.data.model.api.response.LoginResponse
import com.serenity.serenityapp.data.repository.AuthRepository
import com.serenity.serenityapp.pref.SettingPreference
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel constructor(
    private val authRepository: AuthRepository,
    private val settingPreference: SettingPreference
): ViewModel() {
    val token: LiveData<String> = settingPreference
        .getToken()
        .asLiveData()

    private val _loginResult = MutableLiveData<LoginResponse>()
    val loginResult: LiveData<LoginResponse> = _loginResult

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun login(loginRequest: LoginRequest) {
        _loading.value = true
        _error.value = ""

        viewModelScope.launch {
            try {
                val response = authRepository.login(loginRequest)

                if (response.isSuccess()) {
                    _loginResult.value = response
                    saveAuthToken(response.accessToken)
                    saveUserId(response.data.user_id)
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
                        val responseBody = Gson().fromJson(response.errorBody()!!.string(), LoginResponse::class.java)
                        _error.value = responseBody.message ?: "Internal Server Error"
                    }
                }
            } finally {
                _loading.value = false
            }
        }
    }

    private fun saveAuthToken(token: String) {
        viewModelScope.launch {
            settingPreference.saveToken(token)
        }
    }

    private fun saveUserId(userId: Int) {
        viewModelScope.launch {
            settingPreference.saveUserId(userId)
        }
    }
}