package com.multibahana.myapp

import com.multibahana.myapp.data.model.product.ProductDto
import com.multibahana.myapp.domain.repository.ProductRepository
import com.multibahana.myapp.domain.usecase.product.GetProductByIdUseCase
import com.multibahana.myapp.presentation.home.ProductResult
import com.multibahana.myapp.utils.ErrorType
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import retrofit2.Response
import java.io.IOException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class GetProductByIdTest {
    private lateinit var productRepository: ProductRepository
    private lateinit var getProductByIdUseCase: GetProductByIdUseCase

    @Before
    fun setup() {
        productRepository = mockk()
        getProductByIdUseCase = GetProductByIdUseCase(productRepository)
    }

    @Test
    fun `when repository returns success, should return Success`() = runTest {
        val mockResponse = singleProductReponse

        coEvery {
            productRepository.getProductById(1)
        } returns mockResponse

        val result = getProductByIdUseCase(1)

        assertTrue { result is ProductResult.Success }
    }

    @Test
    fun `when repository returns 404, should return Client Error`() = runTest {
        val errorResponse = Response.error<ProductDto>(
            404,
            "Not Found".toResponseBody("application/json".toMediaType())
        )
        coEvery { productRepository.getProductById(1) } returns errorResponse

        val result = getProductByIdUseCase(1)

        assertTrue(result is ProductResult.Error)
        assertEquals(ErrorType.Client, result.errorType)
    }

    @Test
    fun `when repository returns 500, should return Server Error`() = runTest {
        val errorResponse = Response.error<ProductDto>(
            500,
            "Internal Server Error".toResponseBody("application/json".toMediaType())
        )
        coEvery { productRepository.getProductById(1) } returns errorResponse

        val result = getProductByIdUseCase(1)

        assertTrue(result is ProductResult.Error)
        val error = result
        assertEquals(ErrorType.Server, error.errorType)
    }


    @Test
    fun `when repository throws IOException, should return Network Error`() = runTest {
        coEvery { productRepository.getProductById(1) } throws IOException("No internet")

        val result = getProductByIdUseCase(1)

        assertTrue(result is ProductResult.Error)
        val error = result as ProductResult.Error
        assertEquals(ErrorType.Network, error.errorType)
    }

    @Test
    fun `when repository throws unknown Exception, should return Unknown Error`() = runTest {
        coEvery { productRepository.getProductById(1) } throws IllegalStateException("Unexpected")

        val result = getProductByIdUseCase(1)

        assertTrue(result is ProductResult.Error)
        val error = result as ProductResult.Error
        assertEquals(ErrorType.Unknown, error.errorType)
    }
}