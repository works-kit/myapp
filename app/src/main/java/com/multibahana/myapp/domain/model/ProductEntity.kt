package com.multibahana.myapp.domain.model

import com.multibahana.myapp.data.model.product.ProductDto

data class ProductEntity(
    val id: Int,
    val title: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val discountPercentage: Double = 0.0,
    val rating: Double = 0.0,
    val stock: Int = 0,
    val brand: String = "",
    val category: String = "",
    val thumbnail: String = "",
    var images: MutableList<String> = mutableListOf<String>()
)

fun ProductDto.toEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        title = title,
        description = description,
        price = price,
        discountPercentage = discountPercentage,
        rating = rating,
        stock = stock,
        brand = brand,
        category = category,
        thumbnail = thumbnail,
        images = images
    )
}
