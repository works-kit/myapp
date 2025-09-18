package com.multibahana.myapp

import com.multibahana.myapp.data.model.product.ProductDto
import com.multibahana.myapp.data.model.product.ProductResponse
import retrofit2.Response

val dummyProduct = ProductDto(
    id = 1,
    title = "Test Product",
    description = "Sample",
    price = 100.0,
    discountPercentage = 0.0,
    rating = 4.5,
    stock = 10,
    brand = "BrandX",
    category = "CategoryX",
    thumbnail = "url",
    images = mutableListOf("img1", "img2")
)

val singleProductReponse = Response.success(dummyProduct)

val mockResponse = Response.success(
    ProductResponse(
        products = listOf(dummyProduct),
        total = 1,
        skip = 0,
        limit = 10
    )
)