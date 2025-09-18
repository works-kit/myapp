package com.multibahana.myapp.presentation

sealed class SplashState {
    object Loading : SplashState()
    object Authenticated : SplashState()
    object Unauthenticated : SplashState()
}
