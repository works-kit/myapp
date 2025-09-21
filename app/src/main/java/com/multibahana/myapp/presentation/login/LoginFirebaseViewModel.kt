package com.multibahana.myapp.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.multibahana.myapp.domain.usecase.authfirebase.CurrentUserWithFirebaseUseCase
import com.multibahana.myapp.domain.usecase.authfirebase.LoginWithFirebaseUseCase
import com.multibahana.myapp.presentation.SplashState
import com.multibahana.myapp.presentation.profile.state.ProfileResult
import com.multibahana.myapp.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LoginFirebaseViewModel @Inject constructor(
    private val loginWithFirebaseUseCase: LoginWithFirebaseUseCase,
    private val currentUserUseCase: CurrentUserWithFirebaseUseCase,
) : ViewModel() {

    private val _loginState = MutableStateFlow<ResultState<FirebaseUser?>?>(null)
    val loginState = _loginState

    private val _splashState = MutableStateFlow<SplashState>(SplashState.Loading)
    val splashState: StateFlow<SplashState> = _splashState

    init {
        checkSession()
    }

    fun loginWithFirebase(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = ResultState.Loading
            loginWithFirebaseUseCase(email, password) { result ->
                _loginState.value = result
            }
        }
    }

    fun clearLoginFirebaseState() {
        _loginState.value = null
    }

    private fun checkSession() {
        viewModelScope.launch {
            _splashState.value = SplashState.Loading
            val user = currentUserUseCase()
            _splashState.value = if (user is ResultState.Success && user.data != null) {
                SplashState.Authenticated
            } else {
                SplashState.Unauthenticated
            }
        }
    }
}