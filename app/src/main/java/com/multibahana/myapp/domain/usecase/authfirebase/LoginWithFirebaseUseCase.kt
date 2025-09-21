package com.multibahana.myapp.domain.usecase.authfirebase

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.multibahana.myapp.domain.repository.AuthFirebaseRepository
import com.multibahana.myapp.utils.ErrorType
import com.multibahana.myapp.utils.ResultState
import jakarta.inject.Inject
import java.io.IOException

class LoginWithFirebaseUseCase @Inject constructor(
    private val authFirebaseRepository: AuthFirebaseRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        onResult: (ResultState<FirebaseUser?>) -> Unit
    ) {
        authFirebaseRepository.loginWithFirebase(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = task.result?.user
                    onResult(ResultState.Success(user))
                } else {
                    onResult(ResultState.Error(ErrorType.Server, task.exception?.message))
                }
            }
            .addOnSuccessListener { result ->
                val user = result?.user
                onResult(ResultState.Success(user))
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