package com.multibahana.myapp.domain.repository

import com.google.android.gms.tasks.Task
import com.multibahana.myapp.data.model.posts.PostDto

interface PostsRepository {
    suspend fun addPost(userId: String, postDto: PostDto): Task<Void?>

    suspend fun getAllPosts(userId: String): Task<List<PostDto>>

    suspend fun getPostById(userId: String, postId: String): Task<PostDto?>

    suspend fun searchPosts(userId: String, query: String): Task<List<PostDto>>

    suspend fun deletePost(userId: String, postId: String): Task<Void?>

    suspend fun updatePost(userId: String, postId: String, postDto: PostDto): Task<Void?>
}
