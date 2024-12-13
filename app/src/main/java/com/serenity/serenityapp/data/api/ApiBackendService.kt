package com.serenity.serenityapp.data.api

import com.serenity.serenityapp.data.model.api.request.ChallengeRequest
import com.serenity.serenityapp.data.model.api.request.LoginRequest
import com.serenity.serenityapp.data.model.api.request.RefreshTokenRequest
import com.serenity.serenityapp.data.model.api.request.RegisterRequest
import com.serenity.serenityapp.data.model.api.response.ChallengeResponse
import com.serenity.serenityapp.data.model.api.response.LoginResponse
import com.serenity.serenityapp.data.model.api.response.ProfileResponse
import com.serenity.serenityapp.data.model.api.response.RefreshTokenResponse
import com.serenity.serenityapp.data.model.api.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiBackendService {
    @POST("login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginResponse

    @POST("register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): RegisterResponse

    @POST("refresh")
    suspend fun refreshToken(
        @Body refreshTokenRequest: RefreshTokenRequest
    ): RefreshTokenResponse

    @GET("profile/{userId}")
    suspend fun getProfile(
        @Header("Authorization") authorization: String,
        @Path("userId") userId: Int
    ): ProfileResponse

    @Multipart
    @PUT("profile/{userId}")
    suspend fun editProfile(
        @Header("Authorization") authorization: String,
        @Path("userId") userId: Int,
        @Part("name") name: RequestBody,
        @Part("age") age: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody?,
        @Part photo: MultipartBody.Part?,
    ): ProfileResponse

    @GET("{userId}/challenges/")
    suspend fun getChallenges(
        @Header("Authorization") authorization: String,
        @Path("userId") userId: Int
    ): ChallengeResponse

    @POST("{userId}/challenges")
    suspend fun createChallenge(
        @Header("Authorization") authorization: String,
        @Path("userId") userId: Int,
        @Body challengeRequest: ChallengeRequest
    ): ChallengeResponse

    @PUT("{userId}/challenges/{challengeId}")
    suspend fun updateChallenge(
        @Header("Authorization") authorization: String,
        @Path("userId") userId: Int,
        @Path("challengeId") challengeId: Int,
        @Body challengeRequest: ChallengeRequest
    ): ChallengeResponse

    @DELETE("{userId}/challenges/{challengeId}")
    suspend fun deleteChallenge(
        @Header("Authorization") authorization: String,
        @Path("userId") userId: Int,
        @Path("challengeId") challengeId: Int,
    ): ChallengeResponse
}