package com.example.storyapp.data.remote.retrofit

import com.example.storyapp.data.remote.response.*
import com.example.storyapp.model.RegisterRequestBody
import com.example.storyapp.model.LoginRequestBody
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("register")
    fun registerUser(
        @Body requestBody: RegisterRequestBody
    ): Call<RegisterResponse>

    @POST("login")
    fun login(@Body userData: LoginRequestBody): Call<LoginResponse>

    @GET("stories")
    fun getStories(
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("location") location: Int?,
        @Header("Authorization") token: String
    ): Call<StoryResponse>


    @GET("stories")
    suspend fun getStories1(
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("location") location: Int?,
        @Header("Authorization") token: String
    ): StoryResponse


    @GET("stories/{id}")
    fun getDetailsStories(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): Call<DetailsStoryResponse>

    @Multipart
    @POST("stories")
    fun postStories(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody?,
        @Part("lon") lon: RequestBody?,
        @Header("Authorization") token: String
    ): Call<FileUploadResponse>
}