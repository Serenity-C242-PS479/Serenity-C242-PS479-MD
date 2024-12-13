package com.serenity.serenityapp.ui.setup

import android.content.Context
import android.provider.Settings
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.serenity.serenityapp.helper.checkAccessbility
import com.serenity.serenityapp.helper.checkUsageStatsPermission
import com.serenity.serenityapp.service.ActivityMonitorService

class SetupViewModel constructor(
    private val context: Context
): ViewModel() {
    val isAccesbilityEnabled: MutableLiveData<Boolean> = MutableLiveData(false)
    var isDisplayOverAppEnabled: MutableLiveData<Boolean> = MutableLiveData(false)
    var isUsageAccessEnabled: MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        isAccesbilityEnabled.value = context.checkAccessbility(ActivityMonitorService::class.java)
        isDisplayOverAppEnabled.value = Settings.canDrawOverlays(context)
        isUsageAccessEnabled.value = context.checkUsageStatsPermission()
    }
}