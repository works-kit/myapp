package com.multibahana.myapp.data.model.product

import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
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