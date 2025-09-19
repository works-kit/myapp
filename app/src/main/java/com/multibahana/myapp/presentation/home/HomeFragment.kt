package com.multibahana.myapp.presentation.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.multibahana.myapp.R
import com.multibahana.myapp.databinding.FragmentHomeBinding
import com.multibahana.myapp.domain.model.AllProductsEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var productsAdapter: ProductsAdapter


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return _binding?.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productsAdapter = ProductsAdapter(
            AllProductsEntity(
                products = emptyList(),
                limit = 0,
                total = 0,
                skip = 0
            )
        )

        binding.recyclerView.apply {
            adapter = productsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewModel.getProducts(
            limit = 10,
            skip = 10,
            select = "title,price,stock,thumbnail",
            sortBy = "title",
            order = "asc"
        )

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.productListState.collect { state ->
                    when (state) {
                        is AllProductsResult.Loading -> binding.progressBar.visibility =
                            View.VISIBLE

                        is AllProductsResult.Success -> {
                            binding.progressBar.visibility = View.GONE
                            productsAdapter.updateProducts(state.products)
                        }

                        is AllProductsResult.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT)
                                .show()
                        }

                        null -> Unit
                    }
                }
            }
        }

        binding.root.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                binding.searchBar.clearFocus()
                val imm =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.searchBar.windowToken, 0)
            }
            v.performClick()
            false
        }

        binding.recyclerView.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                binding.searchBar.clearFocus()
                val imm =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.searchBar.windowToken, 0)
            }
            v.performClick()
            false
        }

        binding.searchBar.apply {
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.isNullOrEmpty()) {
                        setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.search_24px, 0)
                    } else {
                        setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.close_24px, 0)
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    val editText = v as EditText
                    val drawableEnd = editText.compoundDrawables[2]
                    if (drawableEnd != null &&
                        event.rawX >= (editText.right - drawableEnd.bounds.width() - editText.paddingEnd)
                    ) {
                        if (!editText.text.isNullOrEmpty()) {
                            editText.text.clear()
                            return@setOnTouchListener true
                        }
                    }
                }
                false
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}