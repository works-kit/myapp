package com.multibahana.myapp.presentation.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.multibahana.myapp.R
import com.multibahana.myapp.databinding.ActivityLoginBinding
import com.multibahana.myapp.presentation.MainActivity
import com.multibahana.myapp.presentation.login.state.LoginResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginState.collectLatest { state ->
                    when (state) {
                        is LoginResult.Loading -> {
                            binding.buttonLogin.text = "Loading.."
                            binding.buttonLogin.isEnabled = false
                            binding.textError.visibility = View.GONE
                        }

                        is LoginResult.Success -> {
                            binding.buttonLogin.text = "Login"
                            binding.buttonLogin.isEnabled = true
                            binding.textError.visibility = View.GONE
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            })
                            viewModel.clearLoginState()
                        }

                        is LoginResult.Error -> {
                            binding.buttonLogin.text = "Login"
                            binding.buttonLogin.isEnabled = true
                            binding.textError.apply {
                                visibility = View.VISIBLE
                                text = state.message
                            }
                        }

                        else -> Unit
                    }
                }
            }
        }


        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextUsername.text.toString()
            val password = binding.editTextPassword.text.toString()

            viewModel.login(email, password)
        }
    }
}