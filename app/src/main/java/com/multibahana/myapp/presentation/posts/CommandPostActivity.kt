package com.multibahana.myapp.presentation.posts

import android.os.Bundle
import android.view.View
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

    private var postId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommandPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        postId = intent.getStringExtra("postId")

        postId?.let {
            viewModel.getPostById(it)
        }

        setupView()
        observeViewModel()
    }

    private fun setupView() {
        if (postId != null) {
            binding.btnSavePost.visibility = View.GONE
            binding.btnUpdatePost.visibility = View.VISIBLE
        } else {
            binding.btnSavePost.visibility = View.VISIBLE
            binding.btnUpdatePost.visibility = View.GONE
        }

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

        binding.btnUpdatePost.setOnClickListener {
            val title = binding.etTitle.text.toString().trim()
            val content = binding.etContent.text.toString().trim()

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "Judul dan konten wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val post = PostEntity(
                id = postId ?: "",
                title = title,
                content = content
            )
            viewModel.updatePost(post)
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

        // Tambahkan observer untuk getPostById (mode edit)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.detailPostState.collectLatest { state ->
                    when (state) {
                        is ResultState.Loading -> {

                        }

                        is ResultState.Success -> {
                            val post = state.data
                            binding.etTitle.setText(post?.title)
                            binding.etContent.setText(post?.content)
                        }

                        is ResultState.Error -> {
                            Toast.makeText(
                                this@CommandPostActivity,
                                "Error ambil data: ${state.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> Unit
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.updatePostState.collectLatest { state ->
                    when (state) {
                        is ResultState.Loading -> {
                            binding.btnUpdatePost.text = "Proses..."
                            binding.btnUpdatePost.isEnabled = false
                        }

                        is ResultState.Success -> {
                            binding.btnUpdatePost.text = "UPDATE POST"
                            binding.btnUpdatePost.isEnabled = true
                            Toast.makeText(
                                this@CommandPostActivity,
                                "Post berhasil diperbarui",
                                Toast.LENGTH_SHORT
                            ).show()
                            setResult(RESULT_OK)
                            finish()
                        }

                        is ResultState.Error -> {
                            binding.btnUpdatePost.text = "UPDATE POST"
                            binding.btnUpdatePost.isEnabled = true
                            Toast.makeText(
                                this@CommandPostActivity,
                                "Error: ${state.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> Unit
                    }
                }
            }
        }

    }
}
