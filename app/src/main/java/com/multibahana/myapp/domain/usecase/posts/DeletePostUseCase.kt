package com.multibahana.myapp.domain.usecase.posts

import com.multibahana.myapp.domain.repository.PostsRepository
import com.multibahana.myapp.utils.ErrorType
import com.multibahana.myapp.utils.ResultState
import jakarta.inject.Inject
import java.io.IOException

class DeletePostUseCase @Inject constructor(
    private val postsRepository: PostsRepository
) {
    suspend operator fun invoke(
        userId: String,
        postId: String,
        onResult: (ResultState<Void?>) -> Unit
    ) {
        postsRepository.deletePost(userId, postId)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(ResultState.Success(task.result))
                } else {
                    onResult(
                        ResultState.Error(
                            ErrorType.Server,
                            task.exception?.message ?: "Gagal menghapus post"
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
