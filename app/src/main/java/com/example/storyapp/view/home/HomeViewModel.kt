package com.example.storyapp.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.remote.Repository
import com.example.storyapp.data.remote.Result
import com.example.storyapp.data.remote.response.StoryResponse

class HomeViewModel(private val repository: Repository) : ViewModel() {

    lateinit var listStory: LiveData<Result<StoryResponse>>

    fun getSearchDataUser(page: Int?, size: Int?, location: Int = 0, token: String) {
        listStory = repository.getStory(page, size, location, token)
    }
}