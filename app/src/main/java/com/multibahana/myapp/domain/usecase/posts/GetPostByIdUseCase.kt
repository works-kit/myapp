package com.multibahana.myapp.domain.usecase.posts

import com.multibahana.myapp.domain.model.PostEntity
import com.multibahana.myapp.domain.model.toEntity
import com.multibahana.myapp.domain.repository.PostsRepository
import com.multibahana.myapp.utils.ErrorType
import com.multibahana.myapp.utils.ResultState
import jakarta.inject.Inject
import java.io.IOException

class GetPostByIdUseCase @Inject constructor(
    private val postsRepository: PostsRepository
) {
    suspend operator fun invoke(
        userId: String,
        postId: String,
        onResult: (ResultState<PostEntity?>) -> Unit
    ) {
        postsRepository.getPostById(userId, postId)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val postDto = task.result
                    val entity = postDto?.toEntity() // convert DTO → Entity
                    onResult(ResultState.Success(entity))
                } else {
                    onResult(
                        ResultState.Error(
                            ErrorType.Server,
                            task.exception?.message ?: "Gagal mengambil post dengan id $postId"
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
