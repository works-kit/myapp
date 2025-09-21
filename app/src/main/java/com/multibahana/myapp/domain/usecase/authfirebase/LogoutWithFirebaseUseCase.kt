package com.multibahana.myapp.domain.usecase.authfirebase

import com.google.firebase.auth.FirebaseUser
import com.multibahana.myapp.domain.repository.AuthFirebaseRepository
import com.multibahana.myapp.utils.ErrorType
import com.multibahana.myapp.utils.ResultState
import jakarta.inject.Inject
import java.io.IOException

class LogoutWithFirebaseUseCase @Inject constructor(
    private val authFirebaseRepository: AuthFirebaseRepository
) {
    suspend operator fun invoke(): ResultState<FirebaseUser?> {
        return try {
            authFirebaseRepository.logoutWithFirebase()
            ResultState.Success(null)
        } catch (e: IOException) {
            ResultState.Error(ErrorType.Network, "Tidak ada koneksi internet: ${e.message}")
        } catch (e: Exception) {
            ResultState.Error(ErrorType.Unknown, e.localizedMessage ?: "Unexpected error")
        }
    }
}