package com.multibahana.myapp.presentation.posts

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.multibahana.myapp.databinding.FragmentPostsBinding
import com.multibahana.myapp.utils.ResultState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostsFragment : Fragment() {
    private val viewModel: PostsViewModel by viewModels()

    private var _binding: FragmentPostsBinding? = null
    private val binding get() = _binding!!

    private lateinit var postAdapter: PostAdapter

//    private val addPostLauncher = registerForActivityResult(
//        ActivityResultContracts.StartActivityForResult()
//    ) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            viewModel.getAllPosts()
//        }
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostsBinding.inflate(layoutInflater)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postAdapter = PostAdapter(
            emptyList(),
            onDeletePost = {
                AlertDialog.Builder(requireContext())
                    .setTitle("Hapus Post")
                    .setMessage("Yakin mau hapus post ${it.id} ?")
                    .setPositiveButton("Ya") { _, _ ->
                        viewModel.deletePost(it.id)
                        viewModel.getAllPostsNewest()

                    }
                    .setNegativeButton("Batal", null)
                    .show()
            },
            onEditPost = { post ->
                val intent = Intent(context, CommandPostActivity::class.java)
                startActivity(intent)
            })

        binding.rvPosts.apply {
            adapter = postAdapter
            layoutManager = LinearLayoutManager(context)
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getAllPostsNewest()
        }

        viewModel.getAllPosts()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.listPostsState.collectLatest { state ->
                    when (state) {
                        is ResultState.Loading -> {
                            binding.swipeRefresh.isRefreshing = true
                        }

                        is ResultState.Success -> {
                            binding.swipeRefresh.isRefreshing = false
                            postAdapter.updatePosts(state.data)

                        }

                        is ResultState.Error -> {
                            binding.swipeRefresh.isRefreshing = false
                            Toast.makeText(context, state.message, Toast.LENGTH_SHORT)
                                .show()
                        }

                        else -> Unit
                    }
                }
            }
        }

        binding.fabAddPost.setOnClickListener {
            val intent = Intent(context, CommandPostActivity::class.java)
            startActivity(intent)
//            addPostLauncher.launch(intent)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PostsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}