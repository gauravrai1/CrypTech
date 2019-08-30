package com.ubertech.cryptech.API.Services

import com.ubertech.cryptech.API.Models.Request.LoginRequest
import com.ubertech.cryptech.API.Models.Request.RegistrationRequest
import com.ubertech.cryptech.API.Models.Request.SubmitRequest
import com.ubertech.cryptech.API.Models.Request.VerifyRequest
import com.ubertech.cryptech.API.Models.Response.*
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {


    // Auth
    @POST("auth/login")
    fun requestLogin(@Body params: LoginRequest): Call<LoginResponse>

    @POST("register")
    fun requestRegister(@Body params: RegistrationRequest): Call<RegistrationResponse>

    @POST("verify")
    fun requestVerification(@Header("authorization") auth: String,
                            @Body params: VerifyRequest): Call<VerifyResponse>


    // Main features
    @GET("level")
    fun requestLevel(@Header("authorization") auth: String): Call<LevelResponse>

    @GET("hint")
    fun requestHint(@Header("authorization") auth: String): Call<HintResponse>

    @POST("submit")
    fun submitAnswer(@Header("authorization") auth: String, @Body params: SubmitRequest): Call<VerifyResponse>

    @GET("leaderboard")
    fun requestLeaderBoard(@Header("authorization") auth: String): Call<LeaderboardResponse>


}