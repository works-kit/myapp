package com.multibahana.myapp.presentation.profile.state

import com.multibahana.myapp.domain.model.UserEntity
import com.multibahana.myapp.utils.ErrorType

sealed class ProfileResult {
    data class Success(val user: UserEntity) : ProfileResult()
    data class Error(val errorType: ErrorType, val message: String? = null) : ProfileResult()
    object Loading : ProfileResult()
    object LoggedOut : ProfileResult()
}