package com.example.storyapp.data.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.storyapp.data.remote.response.*
import com.example.storyapp.data.remote.retrofit.ApiService
import com.example.storyapp.model.LoginRequestBody
import com.example.storyapp.model.RegisterRequestBody
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Repository private constructor(
    private val apiService: ApiService,
    ) {

    private fun <T> makeApiCall(apiCall: Call<T>): LiveData<Result<T>> {
        val result = MutableLiveData<Result<T>>()

        result.value = Result.Loading
        apiCall.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    val items = response.body()
                    if (items != null) {
                        result.value = Result.Success(items)
                    } else {
                        result.value = Result.Error("Data not found")
                    }
                } else {
                    result.value = Result.Error("Error ${response.code()}")
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                result.value = Result.Error(t.message ?: "Unknown error")
            }
        })

        return result
    }

    fun getStory(page: Int?, size: Int?, location: Int = 0, token: String): LiveData<Result<StoryResponse>>{
        return makeApiCall(apiService.getStories(page, size, location, "Bearer $token"))
    }

    fun getDetailsStory(id: String, token: String): LiveData<Result<DetailsStoryResponse>>{
        return makeApiCall(apiService.getDetailsStories(id, "Bearer $token"))
    }

    fun postUserRegister(data: RegisterRequestBody) : LiveData<Result<RegisterResponse>>{
        return makeApiCall(apiService.registerUser(data))
    }

    fun postUserLogin(data: LoginRequestBody) : LiveData<Result<LoginResponse>>{
        return makeApiCall(apiService.login(data))
    }

    fun postStory(imageMultipart: MultipartBody.Part, description: RequestBody, token: String): LiveData<Result<FileUploadResponse>>{
        return makeApiCall(apiService.postStories(imageMultipart, description, "Bearer $token"))
    }


    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService

        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService)
            }.also { instance = it }
    }
}