package com.example.storyapp.view.login

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.MainActivity
import com.example.storyapp.data.remote.response.LoginResponse
import com.example.storyapp.data.remote.retrofit.ApiConfig
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.model.UserLogin
import com.example.storyapp.model.UserModel
import com.example.storyapp.model.UserPreferences
import com.example.storyapp.view.ViewModelFactory
import com.example.storyapp.view.register.RegisterActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {
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
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[LoginViewModel::class.java]

        loginViewModel.getUser().observe(this) { user ->
            this.user = user
        }
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
                email != user.email -> {
                    binding.emailEditTextLayout.error = "Email tidak sesuai"
                }
                password != user.password -> {
                    binding.passwordEditTextLayout.error = "Password tidak sesuai"
                }

                else -> {
                    val userLogin = UserLogin(email, password)
                    val client = ApiConfig.getApiService().login(userLogin)
                    client.enqueue(object : Callback<LoginResponse> {
                        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                            if (response.isSuccessful) {
                                val loginResult = response.body()?.loginResult
                                if (loginResult != null) {
                                    val userId = loginResult.userId
                                    val name = loginResult.name
                                    val token = loginResult.token
                                    Log.i("LOGIN", name.toString())
                                    Log.i("LOGIN", userId.toString())
                                    Log.i("LOGIN", token.toString())
                                    name?.let { it1 ->
                                        if (token != null) {
                                            loginViewModel.saveUser(UserModel(it1,email,password, true, token))
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
                            } else {
                                Log.i("LOGIN", "LOGIN FAILED")
                            }
                        }

                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                            Log.i("LOGIN", "LOGIN FAILED BECAUSE NETWORK")
                        }
                    })

                }
            }
        }
    }

    fun clickToRegister(view: View) {
        val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
        startActivity(intent)
    }
}