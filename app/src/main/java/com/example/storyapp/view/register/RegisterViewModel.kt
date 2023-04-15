package com.example.storyapp.view.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.remote.Repository
import com.example.storyapp.data.remote.Result
import com.example.storyapp.data.remote.response.RegisterResponse
import com.example.storyapp.data.remote.response.StoryResponse
import com.example.storyapp.model.RegisterRequestBody
import com.example.storyapp.model.UserModel
import com.example.storyapp.model.UserPreferences
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: Repository) : ViewModel() {
    lateinit var registerPost: LiveData<Result<RegisterResponse>>

    fun postRegister(data: RegisterRequestBody) {
        registerPost = repository.postUserRegister(data)
    }
}