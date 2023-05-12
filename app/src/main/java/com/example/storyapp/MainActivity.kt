package com.example.storyapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.adapter.LoadingStateAdapter
import com.example.storyapp.adapter.StoryListAdapter
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.model.UserModel
import com.example.storyapp.model.UserPreferences
import com.example.storyapp.view.DataSourceManager
import com.example.storyapp.view.ViewModelFactory
import com.example.storyapp.view.login.LoginActivity
import com.example.storyapp.view.maps.MapsActivity
import com.example.storyapp.view.story.StoryActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Suppress("UNCHECKED_CAST")
class MainActivity : AppCompatActivity() {
    private lateinit var mainDataSource: MainDataSource
    private lateinit var binding: ActivityMainBinding
    private lateinit var user: UserModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainDataSource = ViewModelProvider(
            this,
            DataSourceManager(UserPreferences.getInstance(dataStore))
        )[MainDataSource::class.java]

        mainViewModel = viewModels<MainViewModel> {
            ViewModelFactory.getInstance()
        }.value

        mainDataSource.getUser().observe(this) { user ->
            if (!user.isLogin && user.token == "") {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                this.user = user
                binding = ActivityMainBinding.inflate(layoutInflater)
                setContentView(binding.root)
                showRecyclerList()

                fab = binding.fab
                fab.setOnClickListener {
                    val intent = Intent(this, StoryActivity::class.java)
                    intent.putExtra(StoryActivity.TOKEN, user.token)
                    startActivity(intent)

                }
                mainViewModel.getStory(user.token)
                getData()
            }
        }

    }

    private fun getData() {
        val adapter = StoryListAdapter()
        binding.viewCard.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        mainViewModel.story.observe(this) {
            adapter.submitData(lifecycle, it)
        }
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
        return when(item.itemId){
            R.id.action_maps ->{
                Log.i("MAPS", "MASUK KE MAPS INTENT")
                val intent = Intent(this, MapsActivity::class.java)
                intent.putExtra("TOKEN", user.token)
                startActivity(intent)
                return true
            }
            R.id.logout -> {
                mainDataSource.logout()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }


    }
}