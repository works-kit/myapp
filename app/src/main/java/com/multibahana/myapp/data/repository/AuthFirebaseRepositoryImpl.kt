package com.multibahana.myapp.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.multibahana.myapp.domain.repository.AuthFirebaseRepository
import jakarta.inject.Inject

class AuthFirebaseRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthFirebaseRepository {

    override suspend fun registerWithFirebase(
        email: String,
        password: String
    ): Task<AuthResult?> {
        return firebaseAuth.createUserWithEmailAndPassword(email, password)
    }

    override suspend fun loginWithFirebase(
        email: String,
        password: String
    ): Task<AuthResult?> {
        return firebaseAuth.signInWithEmailAndPassword(email, password)
    }

    override suspend fun logoutWithFirebase() {
        firebaseAuth.signOut()
    }

    override suspend fun currentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }
}
