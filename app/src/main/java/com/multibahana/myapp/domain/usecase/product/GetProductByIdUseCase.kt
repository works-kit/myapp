package com.multibahana.myapp.domain.usecase.product

import com.multibahana.myapp.domain.model.toEntity
import com.multibahana.myapp.domain.repository.ProductRepository
import com.multibahana.myapp.presentation.home.ProductResult
import com.multibahana.myapp.utils.ErrorType
import jakarta.inject.Inject
import java.io.IOException
import java.net.SocketTimeoutException

class GetProductByIdUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(id: Int): ProductResult {
        return try {
            val response = productRepository.getProductById(id)

            when {
                response.isSuccessful -> {
                    response.body()?.let { body ->
                        ProductResult.Success(body.toEntity())
                    } ?: ProductResult.Error(
                        ErrorType.Server,
                        "Response body is null ${response.errorBody().toString()}"
                    )
                }

                response.code() in 400..499 -> {
                    ProductResult.Error(
                        ErrorType.Client,
                        "Invalid client input  ${response.errorBody().toString()}"
                    )
                }

                response.code() in 500..599 -> {
                    ProductResult.Error(
                        ErrorType.Server,
                        "Server error ${response.errorBody().toString()}"
                    )
                }

                else -> {
                    ProductResult.Error(
                        ErrorType.Unknown,
                        "Unexpected error ${response.errorBody().toString()}"
                    )
                }
            }
        } catch (e: IOException) {
            ProductResult.Error(ErrorType.Network, "No internet connection : ${e.message}")
        } catch (e: SocketTimeoutException) {
            ProductResult.Error(ErrorType.Timeout, "Connection timeout : ${e.message}")
        } catch (e: Exception) {
            ProductResult.Error(ErrorType.Unknown, e.localizedMessage ?: "Unexpected error")
        }
    }
}