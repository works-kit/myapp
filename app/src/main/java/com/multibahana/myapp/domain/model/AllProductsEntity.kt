package com.multibahana.myapp.domain.model

import com.multibahana.myapp.data.model.product.ProductDto
import com.multibahana.myapp.data.model.product.ProductResponse

data class AllProductsEntity(
    val products: List<ProductDto>,
    val total: Int,
    val skip: Int,
    val limit: Int,
)

fun ProductResponse.toEntity(): AllProductsEntity {
    return AllProductsEntity(
        products = products,
        total = total,
        skip = skip,
        limit = limit
    )
}
