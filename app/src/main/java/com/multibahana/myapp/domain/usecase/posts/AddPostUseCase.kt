package com.multibahana.myapp.domain.usecase.posts

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.multibahana.myapp.data.model.posts.PostDto
import com.multibahana.myapp.domain.repository.PostsRepository
import com.multibahana.myapp.utils.ErrorType
import com.multibahana.myapp.utils.ResultState
import jakarta.inject.Inject
import java.io.IOException

class AddPostUseCase @Inject constructor(
    private val postsRepository: PostsRepository
) {
    suspend operator fun invoke(
        userId: String,
        postDto: PostDto,
        onResult: (ResultState<Void?>) -> Unit
    ) {
        postsRepository.addPost(userId, postDto)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result = task.result
                    onResult(ResultState.Success(result))
                } else {
                    onResult(
                        ResultState.Error(
                            ErrorType.Server,
                            task.exception?.message ?: "Gagal menambahkan post"
                        )
                    )
                }
            }
            .addOnFailureListener { exception ->
                when (exception) {
                    is FirebaseAuthInvalidCredentialsException ->
                        onResult(
                            ResultState.Error(
                                ErrorType.Client,
                                "Data post tidak valid: ${exception.message}"
                            )
                        )

                    is FirebaseAuthUserCollisionException ->
                        onResult(
                            ResultState.Error(
                                ErrorType.Client,
                                "Duplikasi data post: ${exception.message}"
                            )
                        )

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
