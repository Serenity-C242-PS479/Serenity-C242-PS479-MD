package com.serenity.serenityapp.data.repository

import com.serenity.serenityapp.data.api.ApiBackendService
import com.serenity.serenityapp.data.model.api.request.ChallengeRequest
import com.serenity.serenityapp.data.model.api.response.ChallengeResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChallengeRepository private constructor(private val apiBackendService: ApiBackendService) {
    suspend fun getChallenge(token: String, userId: Int): ChallengeResponse {
        return withContext(Dispatchers.IO) {
            apiBackendService.getChallenges(token, userId)
        }
    }

    suspend fun createChallenge(token: String, userId: Int, challengeRequest: ChallengeRequest): ChallengeResponse {
        return withContext(Dispatchers.IO) {
            apiBackendService.createChallenge(token, userId, challengeRequest)
        }
    }

    suspend fun updateChallenge(token:String, userId: Int, challengeId: Int, challengeRequest: ChallengeRequest): ChallengeResponse {
        return withContext(Dispatchers.IO) {
            apiBackendService.updateChallenge(token, userId, challengeId, challengeRequest)
        }
    }

    suspend fun deleteChallenge(token: String, userId: Int, challengeId: Int): ChallengeResponse {
        return withContext(Dispatchers.IO) {
            apiBackendService.deleteChallenge(token, userId, challengeId)
        }
    }


    companion object {
        @Volatile
        private var instance: ChallengeRepository? = null
        fun getInstance(
            apiBackendService: ApiBackendService
        ): ChallengeRepository =
            instance ?: synchronized(this) {
                instance ?: ChallengeRepository(apiBackendService)
            }.also { instance = it }
    }
}