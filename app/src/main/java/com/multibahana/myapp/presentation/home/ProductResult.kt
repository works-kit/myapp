package com.multibahana.myapp.presentation.home

import com.multibahana.myapp.domain.model.AllProductsEntity
import com.multibahana.myapp.domain.model.ProductEntity
import com.multibahana.myapp.utils.ErrorType

sealed class AllProductsResult {
    data class Success(val products: AllProductsEntity) : AllProductsResult()
    data class Error(val errorType: ErrorType, val message: String? = null) : AllProductsResult()
    object Loading : AllProductsResult()
}

sealed class ProductResult {
    data class Success(val product: ProductEntity) : ProductResult()
    data class Error(val errorType: ErrorType, val message: String? = null) : ProductResult()
    object Loading : ProductResult()
}