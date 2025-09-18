package com.multibahana.myapp.data.model.refreshtoken

data class RefreshTokenRequest(
    val refreshToken: String?,
    val expiresInMins: Int = 60 * 24
)