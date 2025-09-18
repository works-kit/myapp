package com.multibahana.myapp.presentation.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.multibahana.myapp.R
import com.multibahana.myapp.databinding.FragmentProfileBinding
import com.multibahana.myapp.presentation.login.LoginActivity
import com.multibahana.myapp.presentation.profile.state.ProfileResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private val viewModel: ProfileViewModel by activityViewModels()

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(layoutInflater)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.accessToken.collectLatest { token ->
                        if (token != null && viewModel.profileState.value !is ProfileResult.Success) {
                            viewModel.getMe(token)
                        }
                    }
                }

                launch {
                    viewModel.profileState.collectLatest { state ->
                        when (state) {
                            is ProfileResult.Loading -> {
                                binding?.apply {
                                    imageViewProfile.setImageResource(R.drawable.ic_launcher_foreground)
                                    textViewName.text = "Loading.."
                                    textViewEmail.text = "Loading.."
                                }
                            }

                            is ProfileResult.Success -> {
                                binding?.apply {
                                    Glide.with(this@ProfileFragment)
                                        .load(state.user.profileImage)
                                        .placeholder(R.drawable.ic_launcher_foreground)
                                        .into(imageViewProfile)

                                    textViewName.text = state.user.username
                                    textViewEmail.text = state.user.email
                                }
                            }

                            is ProfileResult.Error -> {
                                binding?.textError?.text = state.errorType.toString()
                            }

                            is ProfileResult.LoggedOut -> {
                                startActivity(Intent(context, LoginActivity::class.java).apply {
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                })
                            }

                            null -> Unit
                        }
                    }
                }
            }
        }

        binding?.buttonLogout?.setOnClickListener { viewModel.logout() }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}