package com.example.storyapp.view.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.remote.Repository
import com.example.storyapp.data.remote.Result
import com.example.storyapp.data.remote.response.FileUploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryViewModel(private val repository: Repository) : ViewModel() {
    lateinit var storyPost: LiveData<Result<FileUploadResponse>>

    fun postStory(imageMultipart: MultipartBody.Part, description: RequestBody, token: String) {
        storyPost = repository.postStory(imageMultipart, description, token)
    }
}