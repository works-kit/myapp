package com.multibahana.myapp.domain.usecase.authfirebase

import android.net.Uri
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.multibahana.myapp.domain.repository.AuthFirebaseRepository
import com.multibahana.myapp.utils.ErrorType
import com.multibahana.myapp.utils.ResultState
import jakarta.inject.Inject
import java.io.IOException

class RegisterWithFirebaseUseCase @Inject constructor(
    private val authFirebaseRepository: AuthFirebaseRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        displayName: String? = null,
        photoUrl: String? = null,
        onResult: (ResultState<FirebaseUser?>) -> Unit
    ) {
        authFirebaseRepository.registerWithFirebase(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = task.result?.user

                    user?.let {
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .apply {
                                displayName?.let { setDisplayName(it) }
                                photoUrl?.let { setPhotoUri(Uri.parse(it)) }
                            }
                            .build()

                        it.updateProfile(profileUpdates)
                            .addOnCompleteListener { updateTask ->
                                if (updateTask.isSuccessful) {
                                    onResult(ResultState.Success(it))
                                } else {
                                    onResult(
                                        ResultState.Error(
                                            ErrorType.Server,
                                            updateTask.exception?.message
                                        )
                                    )
                                }
                            }
                    } ?: onResult(ResultState.Error(ErrorType.Server, "User is null"))
                } else {
                    onResult(ResultState.Error(ErrorType.Server, task.exception?.message))
                }
            }
            .addOnFailureListener { exception ->
                when (exception) {
                    is FirebaseAuthInvalidCredentialsException ->
                        onResult(
                            ResultState.Error(
                                ErrorType.Client,
                                "Email atau password tidak valid: ${exception.message}"
                            )
                        )

                    is FirebaseAuthUserCollisionException ->
                        onResult(
                            ResultState.Error(
                                ErrorType.Client,
                                "Email sudah digunakan: ${exception.message}"
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
