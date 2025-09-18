package com.multibahana.myapp.data.remote.api

import com.multibahana.myapp.data.model.getme.GetMeResponseDto
import com.multibahana.myapp.data.model.login.LoginRequestDto
import com.multibahana.myapp.data.model.login.LoginResponseDto
import com.multibahana.myapp.data.model.refreshtoken.RefreshTokenRequest
import com.multibahana.myapp.data.model.refreshtoken.TokenResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthService {
    @Headers("Content-Type: application/json")
    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequestDto
    ): Response<LoginResponseDto>

    @GET("auth/me")
    suspend fun getMe(
        @Header("Authorization") token: String
    ): Response<GetMeResponseDto>

    @Headers("Content-Type: application/json")
    @POST("auth/refresh")
    suspend fun refreshToken(
        @Body request: RefreshTokenRequest
    ): Response<TokenResponseDto>
}