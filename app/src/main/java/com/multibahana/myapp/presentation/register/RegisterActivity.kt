package com.multibahana.myapp.presentation.register

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
import com.multibahana.myapp.databinding.ActivityRegisterBinding
import com.multibahana.myapp.utils.ResultState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private val viewModel: RegisterFirebaseViewModel by viewModels()
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    when (state) {
                        is ResultState.Loading -> {
                            binding.buttonSubmit.text = "Loading.."
                            binding.buttonSubmit.isEnabled = false
                            binding.textError.visibility = View.GONE
                        }

                        is ResultState.Success -> {
                            binding.buttonSubmit.text = "Register"
                            binding.buttonSubmit.isEnabled = true
                            binding.textError.visibility = View.GONE
                            binding.textSucces.visibility = View.VISIBLE
                            binding.textSucces.text = "Register berhasil."
                            viewModel.clearRegisterFirebaseState()
                        }

                        is ResultState.Error -> {
                            binding.buttonSubmit.text = "Register"
                            binding.buttonSubmit.isEnabled = true
                            binding.textSucces.visibility = View.GONE
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


        binding.buttonSubmit.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            viewModel.registerWithFirebase(email, password)
        }
    }
}