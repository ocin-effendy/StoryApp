package com.example.storyapp.data.remote.retrofit

import com.example.storyapp.data.remote.response.LoginResponse
import com.example.storyapp.data.remote.response.RegisterResponse
import com.example.storyapp.data.remote.response.StoryResponse
import com.example.storyapp.model.RegisterRequestBody
import com.example.storyapp.model.UserLogin
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("register")
    fun registerUser(
        @Body requestBody: RegisterRequestBody
    ): RegisterResponse

    @POST("login")
    fun login(@Body userData: UserLogin): Call<LoginResponse>

    @GET("stories")
    fun getStories(
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("location") location: Int = 0,
        @Header("Authorization") token: String
    ): Call<StoryResponse>
}