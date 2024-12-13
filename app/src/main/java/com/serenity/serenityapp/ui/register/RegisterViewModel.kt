package com.serenity.serenityapp.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.serenity.serenityapp.data.model.api.request.RegisterRequest
import com.serenity.serenityapp.data.model.api.response.RegisterResponse
import com.serenity.serenityapp.data.repository.AuthRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel constructor(
    private val authRepository: AuthRepository,
): ViewModel() {
    private val _registerResult = MutableLiveData<RegisterResponse>()
    val registerResult: LiveData<RegisterResponse> = _registerResult

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun register(registerRequest: RegisterRequest) {
        _loading.value = true
        _error.value = ""

        viewModelScope.launch {
            try {
                val response = authRepository.register(registerRequest)

                if (response.isSuccess()) {
                    _registerResult.value = response
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