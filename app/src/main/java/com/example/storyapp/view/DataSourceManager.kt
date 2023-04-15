package com.example.storyapp.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.MainDataSource
import com.example.storyapp.model.UserPreferences
import com.example.storyapp.view.login.LoginDataSource

class DataSourceManager(private val pref: UserPreferences) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainDataSource::class.java) -> {
                MainDataSource(pref) as T
            }
            modelClass.isAssignableFrom(LoginDataSource::class.java) -> {
                LoginDataSource(pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }


}