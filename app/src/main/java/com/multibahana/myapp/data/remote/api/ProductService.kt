package com.multibahana.myapp.data.remote.api

import com.multibahana.myapp.data.model.product.ProductDto
import com.multibahana.myapp.data.model.product.ProductResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductService {
    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int? = null,
        @Query("skip") skip: Int? = null,
        @Query("select") select: String? = null,
        @Query("sortBy") sortBy: String? = null,
        @Query("order") order: String? = null
    ): Response<ProductResponse>

    @GET("products/{id}")
    suspend fun getProductById(
        @Path("id") id: Int
    ): Response<ProductDto>

    @GET("products/search")
    suspend fun searchProducts(
        @Query("q") query: String,
        @Query("limit") limit: Int? = null,
        @Query("skip") skip: Int? = null,
        @Query("select") select: String? = null,
        @Query("sortBy") sortBy: String? = null,
        @Query("order") order: String? = null
    ): Response<ProductResponse>

    @Headers("Content-Type: application/json")
    @POST("products/add")
    suspend fun addProduct(
        @Body product: ProductDto
    ): Response<ProductDto>

    @Headers("Content-Type: application/json")
    @PUT("products/{id}")
    suspend fun updateProduct(
        @Path("id") id: Int,
        @Body product: ProductDto
    ): Response<ProductDto>

    @Headers("Content-Type: application/json")
    @PATCH("products/{id}")
    suspend fun patchProduct(
        @Path("id") id: Int,
        @Body updates: Map<String, Any>
    ): Response<ProductDto>

    @DELETE("products/{id}")
    suspend fun deleteProduct(
        @Path("id") id: Int
    ): Response<ProductDto>
}