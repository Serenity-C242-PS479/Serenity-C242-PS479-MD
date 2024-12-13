package com.serenity.serenityapp.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.serenity.serenityapp.pref.SettingPreference

class SplashViewModel constructor(
    private val settingPreference: SettingPreference
): ViewModel() {
    val token: LiveData<String> = settingPreference
        .getToken()
        .asLiveData()
}