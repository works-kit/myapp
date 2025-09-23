package com.multibahana.myapp.data.model.posts

data class PostDto(
    val title: String = "",
    val content: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val userId: String = ""
)