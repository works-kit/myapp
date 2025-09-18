package com.multibahana.myapp.domain.usecase

import com.multibahana.myapp.data.local.prefs.DataStoreManager
import com.multibahana.myapp.domain.model.toEntity
import com.multibahana.myapp.domain.repository.AuthRepository
import com.multibahana.myapp.presentation.profile.state.ProfileResult
import com.multibahana.myapp.utils.ErrorType
import jakarta.inject.Inject
import kotlinx.coroutines.flow.first
import java.io.IOException
import java.net.SocketTimeoutException


class GetMeUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStoreManager: DataStoreManager
) {
    suspend operator fun invoke(token: String): ProfileResult {
        return try {
            if (token.isBlank()) {
                ProfileResult.Error(ErrorType.Unknown, "Access token cannot be empty")
            }

            val response = authRepository.getMe(token)

            when {
                response.isSuccessful -> {
                    response.body()?.let { body ->
                        ProfileResult.Success(body.toEntity())
                    } ?: ProfileResult.Error(ErrorType.Server, "Response server ${response.errorBody().toString()}")
                }

                response.code() == 401 -> {
                    generateNewAccessToken()
                }

                response.code() in 402..499 -> {
                    ProfileResult.Error(
                        ErrorType.Client,
                        "Invalid client Access token ${response.errorBody().toString()}"
                    )
                }

                response.code() in 500..599 -> {
                    ProfileResult.Error(ErrorType.Server, "Server error ${response.errorBody().toString()}")
                }

                else -> {
                    ProfileResult.Error(ErrorType.Unknown, "Unexpected error ${response.errorBody().toString()}")
                }
            }
        } catch (e: IOException) {
            ProfileResult.Error(ErrorType.Network, "No internet connection ${e.message}")
        } catch (e: SocketTimeoutException) {
            ProfileResult.Error(ErrorType.Timeout, "Connection timeout ${e.message}")
        } catch (e: Exception) {
            ProfileResult.Error(ErrorType.Unknown, e.localizedMessage ?: "Unexpected error ${e.message}")
        }
    }

    private suspend fun generateNewAccessToken(): ProfileResult {
        val refreshToken = dataStoreManager.refreshToken.first() ?: return ProfileResult.Error(
            ErrorType.Client,
            "Refresh token is null"
        )

        return try {
            val refreshResponse = authRepository.refreshToken(refreshToken)

            if (refreshResponse.isSuccessful) {
                refreshResponse.body()?.let { newTokens ->
                    dataStoreManager.saveTokens(newTokens.accessToken, newTokens.refreshToken)

                    val retryResponse = authRepository.getMe("Bearer ${newTokens.accessToken}")
                    if (retryResponse.isSuccessful) {
                        retryResponse.body()?.let {
                            ProfileResult.Success(it.toEntity())
                        }
                    }
                }
            }
            ProfileResult.Error(ErrorType.Server, "Refresh token is empty")
        } catch (e: Exception) {
            ProfileResult.Error(ErrorType.Unknown, e.message)
        }
    }
}

