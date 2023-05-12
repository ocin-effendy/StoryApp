package com.example.storyapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.data.remote.Repository
import com.example.storyapp.data.remote.Result
import com.example.storyapp.data.remote.response.ListStoryItem
import com.example.storyapp.data.remote.response.StoryResponse

class MainViewModel(private val repository: Repository) : ViewModel() {

    lateinit var listStory: LiveData<Result<StoryResponse>>

    fun getSearchDataUser(page: Int?, size: Int?, location: Int = 0, token: String) {
        listStory = repository.getStory(page, size, location, token)
    }

    lateinit var story: LiveData<PagingData<ListStoryItem>>

    fun getStory(token: String){
        story = repository.getStoryResponse(token).cachedIn(viewModelScope)
    }


}