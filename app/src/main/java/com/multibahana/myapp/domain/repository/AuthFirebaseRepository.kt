package com.multibahana.myapp.domain.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser

interface AuthFirebaseRepository {
    suspend fun registerWithFirebase(email: String, password: String): Task<AuthResult?>
    suspend fun loginWithFirebase(email: String, password: String): Task<AuthResult?>
    suspend fun logoutWithFirebase()
    suspend fun currentUser(): FirebaseUser?
}
