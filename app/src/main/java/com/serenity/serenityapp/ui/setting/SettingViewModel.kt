package com.serenity.serenityapp.ui.setting

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.serenity.serenityapp.pref.SettingPreference
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SettingViewModel constructor(
    private val settingPreference: SettingPreference
): ViewModel() {
    val isFocusModeEnabled = settingPreference
        .isFocusModeEnabled()
        .asLiveData()

    val isCustomLimitStateEnabled = settingPreference
        .isCustomLimitStateEnabled()
        .asLiveData()

    val customLimitValue = settingPreference
        .getCustomLimitValue()
        .asLiveData()

    fun saveFocusMode(enabled: Boolean) {
        viewModelScope.launch {
            settingPreference.saveFocusMode(enabled)
        }
    }

    fun saveCustomLimitState(enabled: Boolean) {
        viewModelScope.launch {
            settingPreference.saveCustomLimitState(enabled)
        }
    }

    fun saveCustomLimitValue(value: String) {
        viewModelScope.launch {
            settingPreference.saveCustomLimitValue(value)
        }
    }

    fun clearToken() {
        viewModelScope.launch {
            settingPreference.saveToken("")
        }
    }
}