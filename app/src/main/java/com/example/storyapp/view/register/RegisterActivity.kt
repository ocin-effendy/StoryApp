package com.example.storyapp.view.register

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.storyapp.data.remote.Result
import com.example.storyapp.databinding.ActivityRegisterBinding
import com.example.storyapp.model.RegisterRequestBody
import com.example.storyapp.view.ViewModelFactory
import com.example.storyapp.view.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
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
        registerViewModel = viewModels<RegisterViewModel> {
            ViewModelFactory.getInstance(application)
        }.value
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            when {
                name.isEmpty() -> {
                    binding.nameEditTextLayout.error = "Masukkan email"
                }
                email.isEmpty() -> {
                    binding.emailEditTextLayout.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    binding.passwordEditTextLayout.error = "Masukkan password"
                }
                else -> {
                    val requestBody = RegisterRequestBody(
                        name = name,
                        email = email,
                        password = password
                    )
                    registerViewModel.postRegister(requestBody)
                    registerViewModel.registerPost.observe(this){result ->
                        when (result) {
                            is Result.Loading -> {
                                binding.progressBar.visibility = View.VISIBLE
                            }
                            is Result.Success -> {
                                binding.progressBar.visibility = View.GONE
                                AlertDialog.Builder(this@RegisterActivity).apply {
                                setTitle("Yeah!")
                                setMessage("Akunnya sudah jadi nih. Yuk, login dan buat ceritamu!.")
                                setPositiveButton("Lanjut") { _, _ ->
                                    finish()
                                }
                                create()
                                show()
                            }
                            }
                            is Result.Error -> {
                                binding.progressBar.visibility = View.GONE
                                binding.emailEditTextLayout.error = "Email ini sudah ada yaa!"
                                Toast.makeText(this, "Register Gagal! Coba lagi yaa!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }
}