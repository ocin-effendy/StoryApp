package com.example.storyapp.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.remote.Repository
import com.example.storyapp.data.remote.Result
import com.example.storyapp.data.remote.response.LoginResponse
import com.example.storyapp.model.LoginRequestBody

class LoginViewModel(private val repository: Repository) : ViewModel() {
    lateinit var loginPost: LiveData<Result<LoginResponse>>

    fun postLogin(data: LoginRequestBody) {
        loginPost = repository.postUserLogin(data)
    }
}