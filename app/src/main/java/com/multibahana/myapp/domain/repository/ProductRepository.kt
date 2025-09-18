package com.multibahana.myapp.domain.repository

import com.multibahana.myapp.data.model.product.ProductDto
import com.multibahana.myapp.data.model.product.ProductResponse
import retrofit2.Response

interface ProductRepository {

    suspend fun getProducts(
        limit: Int? = null,
        skip: Int? = null,
        select: String? = null,
        sortBy: String? = null,
        order: String? = null
    ): Response<ProductResponse>

    suspend fun getProductById(id: Int): Response<ProductDto>

    suspend fun searchProducts(
        query: String,
        limit: Int? = null,
        skip: Int? = null,
        select: String? = null,
        sortBy: String? = null,
        order: String? = null
    ): Response<ProductResponse>

    suspend fun addProduct(product: ProductDto): Response<ProductDto>

    suspend fun updateProduct(id: Int, product: ProductDto): Response<ProductDto>

    suspend fun patchProduct(id: Int, updates: Map<String, Any>): Response<ProductDto>

    suspend fun deleteProduct(id: Int): Response<ProductDto>
}