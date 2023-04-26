package com.example.storyapp.view.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.remote.Repository
import com.example.storyapp.data.remote.Result
import com.example.storyapp.data.remote.response.RegisterResponse
import com.example.storyapp.model.RegisterRequestBody

class RegisterViewModel(private val repository: Repository) : ViewModel() {
    lateinit var registerPost: LiveData<Result<RegisterResponse>>

    fun postRegister(data: RegisterRequestBody) {
        registerPost = repository.postUserRegister(data)
    }
}