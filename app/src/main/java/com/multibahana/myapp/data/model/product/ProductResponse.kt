package com.multibahana.myapp.data.model.product

data class ProductResponse(
    val products: List<ProductDto>,
    val total : Int,
    val skip : Int,
    val limit : Int,
)
