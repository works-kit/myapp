package com.multibahana.myapp.domain.usecase.posts

import com.multibahana.myapp.domain.model.PostEntity
import com.multibahana.myapp.domain.model.toEntity
import com.multibahana.myapp.domain.repository.PostsRepository
import com.multibahana.myapp.utils.ErrorType
import com.multibahana.myapp.utils.ResultState
import jakarta.inject.Inject
import java.io.IOException

class SearchPostUseCase @Inject constructor(
    private val postsRepository: PostsRepository
) {
    suspend operator fun invoke(
        userId: String,
        query: String,
        onResult: (ResultState<List<PostEntity>>) -> Unit
    ) {
        postsRepository.searchPosts(userId, query)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val posts = task.result ?: emptyList()
                    val entities = posts.map { it.toEntity() }
                    onResult(ResultState.Success(entities))
                } else {
                    onResult(
                        ResultState.Error(
                            ErrorType.Server,
                            task.exception?.message ?: "Gagal mencari post"
                        )
                    )
                }
            }
            .addOnFailureListener { exception ->
                when (exception) {
                    is IOException ->
                        onResult(
                            ResultState.Error(
                                ErrorType.Network,
                                "Tidak ada koneksi internet: ${exception.message}"
                            )
                        )

                    else ->
                        onResult(
                            ResultState.Error(
                                ErrorType.Unknown,
                                exception.localizedMessage ?: "Unexpected error"
                            )
                        )
                }
            }
    }
}