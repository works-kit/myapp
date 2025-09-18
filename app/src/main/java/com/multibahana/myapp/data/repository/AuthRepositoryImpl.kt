package com.multibahana.myapp.data.repository

import com.multibahana.myapp.data.model.getme.GetMeResponseDto
import com.multibahana.myapp.data.model.login.LoginRequestDto
import com.multibahana.myapp.data.model.login.LoginResponseDto
import com.multibahana.myapp.data.model.refreshtoken.RefreshTokenRequest
import com.multibahana.myapp.data.model.refreshtoken.TokenResponseDto
import com.multibahana.myapp.data.remote.api.AuthService
import com.multibahana.myapp.domain.repository.AuthRepository
import jakarta.inject.Inject
import retrofit2.Response

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
) : AuthRepository {
    override suspend fun login(username: String, password: String): Response<LoginResponseDto> {
        return authService.login(LoginRequestDto(username, password))
    }

    override suspend fun getMe(token: String): Response<GetMeResponseDto> {
        return authService.getMe(token)
    }

    override suspend fun refreshToken(refreshToken: String): Response<TokenResponseDto> {
        return authService.refreshToken(RefreshTokenRequest(refreshToken))
    }
}