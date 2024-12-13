package com.serenity.serenityapp.data.repository

import com.serenity.serenityapp.data.api.ApiBackendService
import com.serenity.serenityapp.data.database.ChatDao
import com.serenity.serenityapp.data.database.SentimentDao
import com.serenity.serenityapp.data.database.UserDao
import com.serenity.serenityapp.data.model.api.response.ProfileData
import com.serenity.serenityapp.data.model.api.response.ProfileResponse
import com.serenity.serenityapp.data.model.database.Chat
import com.serenity.serenityapp.data.model.database.Sentiment
import com.serenity.serenityapp.data.model.database.User
import com.serenity.serenityapp.data.model.database.UserWithSentiment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class NotificationRepository private constructor(
    private val userDao: UserDao,
    private val chatDao: ChatDao,
    private val sentimentDao: SentimentDao,
) {
    suspend fun insertUser(user: User): Long {
        return withContext(Dispatchers.IO) {
            userDao.insert(user)
        }
    }

    suspend fun getUsers(): List<User> {
        return withContext(Dispatchers.IO) {
            userDao.getUsers()
        }
    }

    suspend fun getUsersWithSentiments(): List<UserWithSentiment> {
        return withContext(Dispatchers.IO) {
            userDao.getUsersWithSentiments()
        }
    }

    suspend fun getUserByName(userName: String): User? {
        return withContext(Dispatchers.IO) {
            userDao.getUserByName(userName)
        }
    }

    suspend fun insertChat(chat: Chat) {
        return withContext(Dispatchers.IO) {
            chatDao.insert(chat)
        }
    }

    suspend fun getChatsByUserId(userId: Int): List<Chat> {
        return withContext(Dispatchers.IO) {
            chatDao.getChatsByUserId(userId)
        }
    }

    suspend fun deleteAllChat() {
        return withContext(Dispatchers.IO) {
            chatDao.deleteAllChat()
        }
    }

    suspend fun getUnpredictedChatsByUserId(userId: Int): List<Chat> {
        return withContext(Dispatchers.IO) {
            chatDao.getUnpredictedChatsByUserId(userId)
        }
    }

    suspend fun insertSentiment(sentiment: Sentiment) {
        return withContext(Dispatchers.IO) {
            sentimentDao.insert(sentiment)
        }
    }

    suspend fun getSentimentsByUserId(userId: Int): List<Sentiment> {
        return withContext(Dispatchers.IO) {
            sentimentDao.getSentimentsByUserId(userId)
        }
    }

    companion object {
        @Volatile
        private var instance: NotificationRepository? = null
        fun getInstance(
            userDao: UserDao,
            chatDao: ChatDao,
            sentimentDao: SentimentDao,
        ): NotificationRepository =
            instance ?: synchronized(this) {
                instance ?: NotificationRepository(
                    userDao,
                    chatDao,
                    sentimentDao
                )
            }.also { instance = it }
    }
}