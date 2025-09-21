package com.multibahana.myapp.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.multibahana.myapp.domain.usecase.authfirebase.CurrentUserWithFirebaseUseCase
import com.multibahana.myapp.domain.usecase.authfirebase.LogoutWithFirebaseUseCase
import com.multibahana.myapp.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class CurrentUserFirebaseViewModel @Inject constructor(
    private val currentUserUseCase: CurrentUserWithFirebaseUseCase,
    private val logoutUseCase: LogoutWithFirebaseUseCase
) : ViewModel() {

    private val _profileState = MutableStateFlow<ResultState<FirebaseUser?>?>(null)
    val profileState: StateFlow<ResultState<FirebaseUser?>?> = _profileState

    fun currentUserFirebase() {
        if (_profileState.value is ResultState.Success) return

        viewModelScope.launch {
            _profileState.value = ResultState.Loading
            val result = currentUserUseCase()
            _profileState.value = result
        }
    }

    fun logoutWithFireBase() {
        viewModelScope.launch {
            logoutUseCase()
            _profileState.value = ResultState.LoggedOut
        }
    }
}