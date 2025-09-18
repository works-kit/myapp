package com.multibahana.myapp.domain.usecase

import com.multibahana.myapp.data.local.prefs.DataStoreManager
import com.multibahana.myapp.domain.model.toEntity
import com.multibahana.myapp.domain.repository.AuthRepository
import com.multibahana.myapp.presentation.login.state.LoginResult
import com.multibahana.myapp.utils.ErrorType
import jakarta.inject.Inject
import java.io.IOException
import java.net.SocketTimeoutException


class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStoreManager: DataStoreManager
) {
    suspend operator fun invoke(username: String, password: String): LoginResult {
        return try {
            if (username.isBlank() || password.isBlank()) {
                LoginResult.Error(ErrorType.Unknown, "Email or password cannot be empty")
            }

            val response = authRepository.login(username, password)

            when {
                response.isSuccessful -> {
                    response.body()?.let { body ->
                        dataStoreManager.saveTokens(body.accessToken, body.refreshToken)
                        LoginResult.Success(body.toEntity())
                    } ?: LoginResult.Error(ErrorType.Server, "Response body is null ${response.errorBody().toString()}")
                }

                response.code() == 401 -> {
                    LoginResult.Error(ErrorType.Unauthorized, "Invalid credentials ${response.errorBody().toString()}")
                }

                response.code() in 402..499 -> {
                    LoginResult.Error(ErrorType.Client, "Invalid client input ${response.errorBody().toString()}")
                }

                response.code() in 500..599 -> {
                    LoginResult.Error(ErrorType.Server, "Server error ${response.errorBody().toString()}")
                }

                else -> {
                    LoginResult.Error(ErrorType.Unknown, "Unexpected error ${response.errorBody().toString()}")
                }
            }
        } catch (e: IOException) {
            LoginResult.Error(ErrorType.Network, "No internet connection ${e.message}")
        } catch (e: SocketTimeoutException) {
            LoginResult.Error(ErrorType.Timeout, "Connection timeout ${e.message}")
        } catch (e: Exception) {
            LoginResult.Error(ErrorType.Unknown, e.localizedMessage ?: "Unexpected error ${e.message}")
        }
    }
}