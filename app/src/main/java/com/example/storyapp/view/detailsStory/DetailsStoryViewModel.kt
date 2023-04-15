package com.example.storyapp.view.detailsStory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.remote.Repository
import com.example.storyapp.data.remote.response.DetailsStoryResponse
import com.example.storyapp.data.remote.Result

class DetailsStoryViewModel(private val repository: Repository) : ViewModel() {
    lateinit var detailsStory: LiveData<Result<DetailsStoryResponse>>

    fun getDetailDataUser(id: String, token: String) {
        detailsStory = repository.getDetailsStory(id, token)
    }
}