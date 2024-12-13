package com.serenity.serenityapp.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.widget.Button
import android.widget.TextView
import androidx.core.app.NotificationCompat
import com.serenity.serenityapp.R
import com.serenity.serenityapp.data.AppData
import com.serenity.serenityapp.data.model.api.request.ChallengeRequest
import com.serenity.serenityapp.data.model.api.response.ProfileData
import com.serenity.serenityapp.data.repository.ChallengeRepository
import com.serenity.serenityapp.data.repository.PredictionRepository
import com.serenity.serenityapp.data.repository.ProfileRepository
import com.serenity.serenityapp.di.Injection
import com.serenity.serenityapp.helper.StartTime
import com.serenity.serenityapp.helper.buildPredictBreakReminderFeatures
import com.serenity.serenityapp.helper.getOnScreenAppPackage
import com.serenity.serenityapp.helper.getSocialMediaUsages
import com.serenity.serenityapp.pref.SettingPreference
import com.serenity.serenityapp.pref.dataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class ActivityMonitorService : AccessibilityService() {
    private val NOTIFICATION_CHANNEL_ID = "activity_monitor_service_channel"

    private lateinit var settingPreference: SettingPreference
    private lateinit var challengeRepository: ChallengeRepository
    private lateinit var predictionRepository: PredictionRepository
    private lateinit var profileRepository: ProfileRepository
    private val blockedApps = AppData.appCodeList
    private var profile: ProfileData? = null
    private var isPopupVisible = false
    private var job: Job? = null
    private var currentPackageName: String = ""

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        settingPreference = SettingPreference.getInstance(applicationContext.dataStore)
        challengeRepository = Injection.provideChallengeRepository()
        predictionRepository = Injection.providePredictionRepository()
        profileRepository = Injection.provideProfileRepository()

        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            currentPackageName = applicationContext.getOnScreenAppPackage()

            MainScope().launch {
                fetchProfile()
            }
        }
    }

    override fun onInterrupt() {
        // Handle service interruptions
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onServiceConnected() {
        super.onServiceConnected()

        val info = AccessibilityServiceInfo()
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
        info.flags = AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
        serviceInfo = info

        startForeground(1, createNotification())

        job = CoroutineScope(Dispatchers.Default).launch {
            while (isActive) {
                checkCustomLimit()
                checkChallenges()
                delay(2000L)
            }
        }
    }

    private fun createNotification(): Notification {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "Activity Monitor Service",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Channel for Activity Monitor notifications"
            enableLights(true)
            enableVibration(true)
            vibrationPattern = longArrayOf(0, 250, 250, 250)
        }
        notificationManager.createNotificationChannel(channel)

        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Activity Monitor Service Running")
            .setContentText("Monitoring social media activity in the background.")
            .setSmallIcon(R.drawable.icon)
            .build()
    }

    private fun notifyMessage(title: String, message: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.icon)
            .build()

        val notificationId = (System.currentTimeMillis() % 10000).toInt()
        notificationManager.notify(notificationId, notification)
    }

    private fun backToHome(packageName: String, message: String) {
        if (!blockedApps.contains(packageName)) return

        performGlobalAction(GLOBAL_ACTION_HOME)

        if (!isPopupVisible) showPopup(message)
    }

    private fun showPopup(message: String) {
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = layoutInflater.inflate(R.layout.popup_message, null)

        popupView.findViewById<TextView>(R.id.messageTextView).text = message

        popupView.findViewById<Button>(R.id.closeButton).setOnClickListener {
            windowManager.removeView(popupView)
            isPopupVisible = false
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        params.gravity = Gravity.CENTER

        windowManager.addView(popupView, params)
        isPopupVisible = true
    }

    private suspend fun checkCustomLimit() {
        val packageName = currentPackageName

        if (!blockedApps.contains(packageName)) return

        settingPreference = SettingPreference.getInstance(applicationContext.dataStore)
        predictionRepository = Injection.providePredictionRepository()

        val isFocusModeEnabled = settingPreference.isFocusModeEnabled().first()
        val isCustomLimitStateEnabled = settingPreference.isCustomLimitStateEnabled().first()
        val customLimitValue = settingPreference.getCustomLimitValue().first()

        if (isFocusModeEnabled) {
            withContext(Dispatchers.Main) {
                backToHome(packageName, "You are not allowed to using social media while Focus Mode enabled")
            }
            return
        }

        if (isCustomLimitStateEnabled && customLimitValue.isNotEmpty()) {
            val activities = applicationContext.getSocialMediaUsages(StartTime.TODAY)
            val totalTimeUsed = activities.sumOf { it.totalTimeUsed }

            val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
            val localTime = LocalTime.parse(customLimitValue, timeFormatter)
            val limitTimeUsed = localTime.toSecondOfDay()

            if (totalTimeUsed > limitTimeUsed) {
                withContext(Dispatchers.Main) {
                    backToHome(packageName, "Your social media usages has reached customized limit")
                }
            }
            return
        }

        val isAlreadyBreak = settingPreference.isAlreadyBreak().first()
        if (isAlreadyBreak) {
            withContext(Dispatchers.Main) {
                backToHome(packageName, "Your social media usages has reached limit")
            }
            return
        }

        val predictionFeatures = applicationContext.buildPredictBreakReminderFeatures(profile)
        val predictionResult = predictionRepository.predictBreakReminder(predictionFeatures)
        val isNoNeedBreak = predictionResult.isNoNeedBreak()

        if (!isNoNeedBreak) {
            settingPreference.refreshCurrentBreakDate()
            withContext(Dispatchers.Main) {
                backToHome(packageName, "Your social media usages has reached limit")
            }
            return
        }
    }

    private suspend fun fetchProfile() {
        try {
            val token = settingPreference.getToken().first()
            val userId = settingPreference.getUserId().first()

            val response = profileRepository.getProfile(token, userId)
            profile = response.data
        } catch (exception: HttpException) {
            Log.e("ActivityMonitorService", "checkChallenges: " + exception.message())
        }
    }

    private suspend fun checkChallenges() {
        try {
            settingPreference = SettingPreference.getInstance(applicationContext.dataStore)
            challengeRepository = Injection.provideChallengeRepository()

            val token = settingPreference.getToken().first()
            val userId = settingPreference.getUserId().first()
            val isSocialMediaOpen = blockedApps.contains(currentPackageName)

            val response = challengeRepository.getChallenge(token, userId)
            val challenges = response.datas
            val onProgressChallenges = challenges.filter { it.status == "On Progress"}

            for (challenge in onProgressChallenges) {
                val challengeId = challenge.id

                val startTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
                val startLocalTime = LocalTime.parse(challenge.startHour(), startTimeFormatter)
                val startTime = startLocalTime.toSecondOfDay()

                val endTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
                val endLocalTime = LocalTime.parse(challenge.endHour(), endTimeFormatter)
                val endTime = endLocalTime.toSecondOfDay()

                val currentLocalTime = LocalTime.now()
                val currentSecondOfDay = currentLocalTime.toSecondOfDay()

                val challengeRequest = ChallengeRequest(
                    userId,
                    challenge.title,
                    challenge.startHour(),
                    challenge.endHour(),
                    "On Progress"
                )

                if (currentSecondOfDay in (startTime + 1)..<endTime && isSocialMediaOpen) {
                    challengeRequest.status = "Failed";
                    challengeRepository.updateChallenge(token, userId, challengeId, challengeRequest)
                    notifyMessage("Challenge Failed", "You failed to pass the challenge (${challenge.title})")
                }

                if (currentSecondOfDay > endTime && !isSocialMediaOpen) {
                    challengeRequest.status = "Passed"
                    challengeRepository.updateChallenge(token, userId, challengeId, challengeRequest)
                    notifyMessage("Challenge Passed", "You successfully pass the challenge (${challenge.title})")
                }
            }
        } catch (exception: HttpException) {
            Log.e("ActivityMonitorService", "checkChallenges: " + exception.message())
        }
    }
}
