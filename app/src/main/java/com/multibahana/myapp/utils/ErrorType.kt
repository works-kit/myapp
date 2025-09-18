package com.multibahana.myapp.utils

sealed class ErrorType {
    object Network : ErrorType()
    object Timeout : ErrorType()
    object Unauthorized : ErrorType()
    object Server : ErrorType()
    object Client : ErrorType()
    object Unknown : ErrorType()
}