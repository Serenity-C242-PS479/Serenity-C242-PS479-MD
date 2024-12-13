package com.serenity.serenityapp.service

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.serenity.serenityapp.data.model.database.Chat
import com.serenity.serenityapp.data.model.database.Sentiment
import com.serenity.serenityapp.data.model.database.User
import com.serenity.serenityapp.di.Injection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException

class NotificationListenerService : NotificationListenerService() {
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)
    private val interval = 4 * 60 * 1000L

    override fun onCreate() {
        super.onCreate()
        serviceScope.launch {
            while (serviceJob.isActive) {
                predictSentiment()
                delay(interval)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)
        val packageName = sbn.packageName

        Log.d("NotificationListenerService", "packageName: $packageName")

        if (packageName != "com.whatsapp") return

        MainScope().launch {
            storeNotification(sbn)
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        super.onNotificationRemoved(sbn)
    }

    private suspend fun storeNotification(sbn: StatusBarNotification) {
        val userName = sbn.notification.extras.getString("android.title")
        val message = sbn.notification.extras.getString("android.text")

        if (userName.isNullOrEmpty() || message.isNullOrEmpty()) return

        if (userName == "WhatsApp") return

        val notificationRepository = Injection.provideNotificationRepository(application)

        var user: User? = notificationRepository.getUserByName(userName)
        if (user == null) {
            user = User(null, userName)

            val userId = notificationRepository.insertUser(user)
            user = User(userId.toInt(), userName)
        }

        val chat = Chat(null, user.id!!, message)
        notificationRepository.insertChat(chat)
    }

    private suspend fun predictSentiment() {
        val notificationRepository = Injection.provideNotificationRepository(application)
        val predictionRepository = Injection.providePredictionRepository()

        val users = notificationRepository.getUsers()

        for (user in users) {
            if (user.id == null) continue

            try {
                val unpredictedChats = notificationRepository.getUnpredictedChatsByUserId(user.id)
                val messageToPredict = unpredictedChats.joinToString(" ") { it.message }

                val predictionFeatures = mapOf(
                    "text" to messageToPredict
                )
                val predictionResponse = predictionRepository.predictSentiment(predictionFeatures)
                val predictedAt = System.currentTimeMillis()

                val sentiment = Sentiment(null, user.id, predictionResponse.sentiment, predictedAt)
                notificationRepository.insertSentiment(sentiment)
            } catch (exception: HttpException) {
                Log.e("NotificationListenerService", "predictSentiment => ${exception.message()}")
            }
        }

        notificationRepository.deleteAllChat()
    }
}
