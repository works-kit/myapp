package com.multibahana.myapp.utils

sealed class ResultState<out T> {
    object Loading : ResultState<Nothing>()
    object LoggedOut : ResultState<Nothing>()
    data class Success<out T>(val data: T) : ResultState<T>()
    data class Error(val errorType: ErrorType, val message: String? = null) : ResultState<Nothing>()
}
