package com.multibahana.myapp.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.multibahana.myapp.domain.usecase.authfirebase.RegisterWithFirebaseUseCase
import com.multibahana.myapp.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class RegisterFirebaseViewModel @Inject constructor(
    private val registerWithFirebaseUseCase: RegisterWithFirebaseUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<ResultState<FirebaseUser?>?>(null)
    val state: StateFlow<ResultState<FirebaseUser?>?> = _state

    fun registerWithFirebase(email: String, password: String) {
        viewModelScope.launch {
            _state.value = ResultState.Loading
            registerWithFirebaseUseCase(
                email = email,
                password = password,
                displayName = email.split("@").get(0),
                photoUrl = "https://i.pravatar.cc/150?img=3"
            ) { result ->
                _state.value = result
            }
        }
    }

    fun clearRegisterFirebaseState() {
        _state.value = null
    }


}