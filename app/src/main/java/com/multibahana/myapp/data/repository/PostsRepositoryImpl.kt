package com.multibahana.myapp.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.multibahana.myapp.data.model.posts.PostDto
import com.multibahana.myapp.domain.repository.PostsRepository
import jakarta.inject.Inject

class PostsRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : PostsRepository {

    override suspend fun addPost(
        userId: String,
        postDto: PostDto
    ): Task<Void?> {
        val docRef = firestore.collection("users")
            .document(userId)
            .collection("posts")
            .document()

        return docRef.set(postDto.copy(userId = userId, id = docRef.id))
            .continueWith { task ->
                if (task.isSuccessful) null else throw task.exception ?: Exception("Gagal menambahkan post")
            }
    }

    override suspend fun getAllPosts(userId: String): Task<List<PostDto>> {
        return firestore.collection("users")
            .document(userId)
            .collection("posts")
            .get()
            .continueWith { task ->
                if (task.isSuccessful) {
                    task.result?.toObjects(PostDto::class.java) ?: emptyList()
                } else {
                    throw task.exception ?: Exception("Gagal mengambil posts")
                }
            }
    }

    override suspend fun getPostById(
        userId: String,
        postId: String
    ): Task<PostDto?> {
        return firestore.collection("users")
            .document(userId)
            .collection("posts")
            .document(postId)
            .get()
            .continueWith { task ->
                if (task.isSuccessful) {
                    task.result?.toObject(PostDto::class.java)
                } else {
                    throw task.exception ?: Exception("Gagal mengambil post $postId")
                }
            }
    }

    override suspend fun searchPosts(
        userId: String,
        query: String
    ): Task<List<PostDto>> {
        return firestore.collection("users")
            .document(userId)
            .collection("posts")
            .whereGreaterThanOrEqualTo("title", query)
            .whereLessThanOrEqualTo("title", query + "\uf8ff")
            .get()
            .continueWith { task ->
                if (task.isSuccessful) {
                    task.result?.toObjects(PostDto::class.java) ?: emptyList()
                } else {
                    throw task.exception ?: Exception("Gagal mencari posts dengan query $query")
                }
            }
    }

    override suspend fun deletePost(
        userId: String,
        postId: String
    ): Task<Void?> {
        return firestore.collection("users")
            .document(userId)
            .collection("posts")
            .document(postId)
            .delete()
            .continueWith { task ->
                if (task.isSuccessful) null else throw task.exception ?: Exception("Gagal menghapus post $postId")
            }
    }

    override suspend fun updatePost(
        userId: String,
        postId: String,
        postDto: PostDto
    ): Task<Void?> {
        return firestore.collection("users")
            .document(userId)
            .collection("posts")
            .document(postId)
            .set(postDto.copy(userId = userId))
            .continueWith { task ->
                if (task.isSuccessful) null else throw task.exception ?: Exception("Gagal memperbarui post $postId")
            }
    }
}
