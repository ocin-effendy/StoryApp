package com.example.storyapp.di

import com.example.storyapp.data.remote.Repository
import com.example.storyapp.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(): Repository {
        val apiService = ApiConfig.getApiService()
        return Repository.getInstance(apiService)
    }
}