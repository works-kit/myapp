package com.multibahana.myapp.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multibahana.myapp.data.local.prefs.DataStoreManager
import com.multibahana.myapp.domain.usecase.LoginUseCase
import com.multibahana.myapp.domain.usecase.GetMeUseCase
import com.multibahana.myapp.presentation.SplashState
import com.multibahana.myapp.presentation.login.state.LoginResult
import com.multibahana.myapp.presentation.profile.state.ProfileResult
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val getMeUseCase: GetMeUseCase,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginResult?>(null)
    val loginState: StateFlow<LoginResult?> = _loginState

    private val _splashState = MutableStateFlow<SplashState>(SplashState.Loading)
    val splashState: StateFlow<SplashState> = _splashState

    init {
        checkSession()
    }


    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginResult.Loading
            val result = loginUseCase(username, password)
            _loginState.value = result
        }
    }

    fun clearLoginState() {
        _loginState.value = null
    }

    private fun checkSession() {
        viewModelScope.launch {
            dataStoreManager.accessToken.collect { token ->
                if (token.isNullOrEmpty()) {
                    _splashState.value = SplashState.Unauthenticated
                } else {
                    _splashState.value = SplashState.Loading
                    val result = getMeUseCase(token)
                    when (result) {
                        is ProfileResult.Success -> _splashState.value = SplashState.Authenticated
                        else -> _splashState.value = SplashState.Unauthenticated
                    }
                }
            }
        }
    }

}