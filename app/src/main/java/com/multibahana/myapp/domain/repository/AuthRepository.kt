package com.multibahana.myapp.domain.repository

import com.multibahana.myapp.data.model.getme.GetMeResponseDto
import com.multibahana.myapp.data.model.login.LoginResponseDto
import com.multibahana.myapp.data.model.refreshtoken.TokenResponseDto
import retrofit2.Response

interface AuthRepository {
    suspend fun login(username: String, password: String): Response<LoginResponseDto>
    suspend fun getMe(token: String): Response<GetMeResponseDto>
    suspend fun refreshToken(refreshToken: String): Response<TokenResponseDto>
}
