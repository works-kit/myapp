package com.multibahana.myapp.domain.model

import com.google.firebase.firestore.DocumentSnapshot
import com.multibahana.myapp.data.model.posts.PostDto

data class PostEntity(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val userId: String = ""
)

fun PostDto.toEntity(): PostEntity {
    return PostEntity(
        title = title,
        content = content,
        createdAt = createdAt,
        userId = userId
    )
}
