package com.multibahana.myapp.presentation.posts

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.multibahana.myapp.databinding.ActivityCommandPostBinding
import com.multibahana.myapp.domain.model.PostEntity
import com.multibahana.myapp.utils.ResultState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CommandPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommandPostBinding
    private val viewModel: PostsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommandPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        observeViewModel()
    }

    private fun setupView() {
        binding.btnSavePost.setOnClickListener {
            val title = binding.etTitle.text.toString().trim()
            val content = binding.etContent.text.toString().trim()

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "Judul dan konten wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val post = PostEntity(
                title = title,
                content = content
            )

            viewModel.addPost(post)
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.addPostState.collectLatest { state ->
                    when (state) {
                        is ResultState.Loading -> {
                            binding.btnSavePost.text = "Procces.."
                            binding.btnSavePost.isEnabled = false
                        }

                        is ResultState.Success -> {
                            binding.btnSavePost.text = "SIMPAN POST"
                            binding.btnSavePost.isEnabled = true
                            binding.etTitle.text?.clear()
                            binding.etContent.text?.clear()
                            Toast.makeText(
                                this@CommandPostActivity,
                                "Post berhasil ditambahkan",
                                Toast.LENGTH_SHORT
                            ).show()
                            viewModel.clearPosts()
//                            setResult(RESULT_OK)
//                            finish()
                        }

                        is ResultState.Error -> {
                            binding.btnSavePost.text = "SIMPAN POST"
                            binding.btnSavePost.isEnabled = true
                            Toast.makeText(
                                this@CommandPostActivity,
                                "Error: ${state.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                            viewModel.clearPosts()
                        }

                        else -> Unit
                    }
                }
            }
        }
    }
}
