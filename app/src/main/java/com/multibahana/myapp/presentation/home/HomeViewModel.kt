package com.multibahana.myapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multibahana.myapp.domain.usecase.product.GetAllProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllProductsUseCase: GetAllProductsUseCase
) : ViewModel() {

    private val _productListState = MutableStateFlow<AllProductsResult?>(null)
    val productListState: StateFlow<AllProductsResult?> = _productListState

    fun getProducts(
        limit: Int? = null,
        skip: Int? = null,
        select: String? = null,
        sortBy: String? = null,
        order: String? = null
    ) {
        if (_productListState.value is AllProductsResult.Success) return

        viewModelScope.launch {
            _productListState.value = AllProductsResult.Loading
            val result = getAllProductsUseCase(limit, skip, select, sortBy, order)
            _productListState.value = result
        }
    }
}