package com.example.storyapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.adapter.StoryAdapter
import com.example.storyapp.data.remote.response.ListStoryItem
import com.example.storyapp.data.remote.response.StoryResponse
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.model.UserModel
import com.example.storyapp.model.UserPreferences
import com.example.storyapp.view.ViewModelBuilder
import com.example.storyapp.view.ViewModelFactory
import com.example.storyapp.view.home.HomeViewModel
import com.example.storyapp.view.login.LoginActivity
import kotlinx.coroutines.*
import com.example.storyapp.data.remote.Result


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Suppress("UNCHECKED_CAST")
class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var user: UserModel
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[MainViewModel::class.java]

        homeViewModel = viewModels<HomeViewModel> {
            ViewModelBuilder.getInstance(application)
        }.value

        mainViewModel.getUser().observe(this) { user ->
            if (!user.isLogin && user.token == "") {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                this.user = user
                binding = ActivityMainBinding.inflate(layoutInflater)
                setContentView(binding.root)
                showRecyclerList()

                homeViewModel.getSearchDataUser(null, null, 0, user.token)

                homeViewModel.listStory.observe(this){result ->
                    when (result) {
                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            binding.progressBar.visibility = View.GONE
                            val data = result.data
                            setData(data)
                        }
                        is Result.Error -> {
                            binding.progressBar.visibility = View.GONE
                        }
                    }
                }


            }
        }

    }

    private fun setData(response: StoryResponse) {
        val adapter = StoryAdapter(response.listStory as List<ListStoryItem>)
        binding.viewCard.adapter = adapter
    }

    private fun showRecyclerList() {
        val layoutManager = LinearLayoutManager(this)
        binding.viewCard.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.viewCard.addItemDecoration(itemDecoration)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        mainViewModel.logout()
        return super.onOptionsItemSelected(item)
    }
}