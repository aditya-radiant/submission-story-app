package com.dicoding.picodiploma.submission_story_app.data.api

import com.dicoding.picodiploma.submission_story_app.data.response.LoginResponse
import com.dicoding.picodiploma.submission_story_app.data.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService{

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

}