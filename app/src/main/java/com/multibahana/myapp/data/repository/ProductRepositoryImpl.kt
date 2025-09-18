package com.multibahana.myapp.data.repository

import com.multibahana.myapp.data.model.product.ProductDto
import com.multibahana.myapp.data.model.product.ProductResponse
import com.multibahana.myapp.data.remote.api.ProductService
import com.multibahana.myapp.domain.repository.ProductRepository
import jakarta.inject.Inject
import retrofit2.Response

class ProductRepositoryImpl @Inject constructor(
    private val productService: ProductService
) : ProductRepository {
    override suspend fun getProducts(
        limit: Int?,
        skip: Int?,
        select: String?,
        sortBy: String?,
        order: String?
    ): Response<ProductResponse> {
        return productService.getProducts(limit, skip, select, sortBy, order)
    }

    override suspend fun getProductById(id: Int): Response<ProductDto> {
        return productService.getProductById(id)
    }

    override suspend fun searchProducts(
        query: String,
        limit: Int?,
        skip: Int?,
        select: String?,
        sortBy: String?,
        order: String?
    ): Response<ProductResponse> {
        return productService.searchProducts(query, limit, skip, select, sortBy, order)
    }

    override suspend fun addProduct(product: ProductDto): Response<ProductDto> {
        return productService.addProduct(product)
    }

    override suspend fun updateProduct(
        id: Int,
        product: ProductDto
    ): Response<ProductDto> {
        return productService.updateProduct(id, product)
    }

    override suspend fun patchProduct(
        id: Int,
        updates: Map<String, Any>
    ): Response<ProductDto> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteProduct(id: Int): Response<ProductDto> {
        return productService.deleteProduct(id)
    }
}