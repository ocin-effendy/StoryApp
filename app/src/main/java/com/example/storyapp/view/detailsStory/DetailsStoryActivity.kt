package com.example.storyapp.view.detailsStory

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.storyapp.MainDataSource
import com.example.storyapp.databinding.ActivityDetailsStoryBinding
import com.example.storyapp.model.UserModel
import com.example.storyapp.model.UserPreferences
import com.example.storyapp.view.ViewModelFactory
import com.example.storyapp.view.DataSourceManager
import com.example.storyapp.data.remote.Result
import com.example.storyapp.data.remote.response.DetailsStoryResponse

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class DetailsStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsStoryBinding
    private lateinit var detailsStoryViewModel: DetailsStoryViewModel
    private lateinit var mainDataSource: MainDataSource
    private lateinit var user: UserModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainDataSource = ViewModelProvider(
            this,
            DataSourceManager(UserPreferences.getInstance(dataStore))
        )[MainDataSource::class.java]
        mainDataSource.getUser().observe(this) { user ->

            this.user = user
            binding = ActivityDetailsStoryBinding.inflate(layoutInflater)
            setContentView(binding.root)
            val userId = intent.getStringExtra(USERID)

            detailsStoryViewModel = viewModels<DetailsStoryViewModel> {
                ViewModelFactory.getInstance(application)
            }.value

            user.token.let {
                detailsStoryViewModel.getDetailDataUser(userId!!, it)
            }

            detailsStoryViewModel.detailsStory.observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            binding.progressBar.visibility = View.GONE
                            val data = result.data
                            setDataDetailStory(data)
                        }
                        is Result.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this@DetailsStoryActivity, "Data tidak tersedia!", Toast.LENGTH_SHORT).show()

                        }
                    }
                }
            }

        }
    }

    @SuppressLint("SetTextI18n")
    private fun setDataDetailStory(item: DetailsStoryResponse) {
        Glide.with(this)
            .load(item.story?.photoUrl)
            .into(binding.imageStory)

        binding.apply {
            nameUser.text = item.story?.name
            descriptionUser.text = item.story?.description
        }

    }
    companion object {
        const val USERID = "USERID"
    }
}