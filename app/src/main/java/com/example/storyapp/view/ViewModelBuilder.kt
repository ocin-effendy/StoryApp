package com.example.storyapp.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.data.remote.Repository
import com.example.storyapp.di.Injection
import com.example.storyapp.view.home.HomeViewModel

class ViewModelBuilder private constructor(private val repository: Repository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }


    companion object {
        @Volatile
        private var instance: ViewModelBuilder? = null
        fun getInstance(context: Context): ViewModelBuilder =
            instance ?: synchronized(this) {
                instance ?: ViewModelBuilder(Injection.provideRepository(context))
            }.also { instance = it }
    }
}