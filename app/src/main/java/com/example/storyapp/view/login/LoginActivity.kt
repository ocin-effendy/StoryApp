package com.example.storyapp.view.login

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.MainActivity
import com.example.storyapp.data.remote.Result
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.model.LoginRequestBody
import com.example.storyapp.model.UserModel
import com.example.storyapp.model.UserPreferences
import com.example.storyapp.view.ViewModelFactory
import com.example.storyapp.view.DataSourceManager
import com.example.storyapp.view.register.RegisterActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {
    private lateinit var loginDataSource: LoginDataSource
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var user: UserModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupViewModel()
        setupAction()

    }


    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupViewModel() {
        loginDataSource = ViewModelProvider(
            this,
            DataSourceManager(UserPreferences.getInstance(dataStore))
        )[LoginDataSource::class.java]

        loginDataSource.getUser().observe(this) { user ->
            this.user = user
        }

        loginViewModel = viewModels<LoginViewModel> {
            ViewModelFactory.getInstance(application)
        }.value
    }

    private fun setupAction() {

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            when {
                email.isEmpty() -> {
                    binding.emailEditTextLayout.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    binding.passwordEditTextLayout.error = "Masukkan password"
                }

                else -> {
                    val loginRequestBody = LoginRequestBody(email, password)
                    loginViewModel.postLogin(loginRequestBody)
                    loginViewModel.loginPost.observe(this){ result ->
                        when (result) {
                            is Result.Loading -> {
                                binding.progressBar.visibility = View.VISIBLE
                            }
                            is Result.Success -> {
                                binding.progressBar.visibility = View.GONE
                                val data = result.data
                                if(data.loginResult != null){
                                    val name = data.loginResult.name
                                    val token = data.loginResult.token

                                    name?.let { it ->
                                        if (token != null) {
                                            loginDataSource.saveUser(UserModel(it,email,password, true, token))
                                        }
                                    }
                                    AlertDialog.Builder(this@LoginActivity).apply {
                                        setTitle("Yeah!")
                                        setMessage("Anda berhasil login. Yuk langsung share ceritamu!")
                                        setPositiveButton("Lanjut") { _, _ ->
                                            val intent = Intent(context, MainActivity::class.java)
                                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                            startActivity(intent)
                                            finish()
                                        }
                                        create()
                                        show()
                                    }

                                }
                            }
                            is Result.Error -> {
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(this@LoginActivity, "Coba cek email dan password dengan benar!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }

    fun clickToRegister(view: View) {
        val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
        startActivity(intent)
    }
}