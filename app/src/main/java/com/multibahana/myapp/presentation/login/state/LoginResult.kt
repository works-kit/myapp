package com.multibahana.myapp.presentation.login.state

import com.multibahana.myapp.domain.model.UserEntity
import com.multibahana.myapp.utils.ErrorType

sealed class LoginResult {
    data class Success(val user: UserEntity) : LoginResult()
    data class Error(val errorType: ErrorType, val message: String? = null) : LoginResult()
    object Loading : LoginResult()
}