package com.multibahana.myapp.domain.usecase.product

import com.multibahana.myapp.domain.model.toEntity
import com.multibahana.myapp.domain.repository.ProductRepository
import com.multibahana.myapp.presentation.home.AllProductsResult
import com.multibahana.myapp.utils.ErrorType
import jakarta.inject.Inject
import java.io.IOException
import java.net.SocketTimeoutException

class GetAllProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(
        limit: Int? = null,
        skip: Int? = null,
        select: String? = null,
        sortBy: String? = null,
        order: String? = null
    ): AllProductsResult {
        return try {
            val response = productRepository.getProducts(limit, skip, select, sortBy, order)

            when {
                response.isSuccessful -> {
                    response.body()?.let { body ->
                        AllProductsResult.Success(body.toEntity())
                    } ?: AllProductsResult.Error(ErrorType.Unknown, "Response body is null : ${response.errorBody()}")
                }

                response.code() in 400..499 -> {
                    AllProductsResult.Error(
                        ErrorType.Client,
                        "Invalid client input  ${response.errorBody()}"
                    )
                }

                response.code() in 500..599 -> {
                    AllProductsResult.Error(ErrorType.Server, "Server error ${response.errorBody()}")
                }

                else -> {
                    AllProductsResult.Error(
                        ErrorType.Unknown,
                        "Unexpected error ${response.errorBody()}"
                    )
                }
            }
        } catch (e: IOException) {
            AllProductsResult.Error(ErrorType.Network, "No internet connection : ${e.message}")
        } catch (e: SocketTimeoutException) {
            AllProductsResult.Error(ErrorType.Timeout, "Connection timeout : ${e.message}")
        } catch (e: Exception) {
            AllProductsResult.Error(ErrorType.Unknown, e.localizedMessage ?: "Unexpected error")
        }
    }
}