package com.multibahana.myapp

import com.multibahana.myapp.data.model.product.ProductResponse
import com.multibahana.myapp.domain.repository.ProductRepository
import com.multibahana.myapp.domain.usecase.product.SearchProductsUseCase
import com.multibahana.myapp.presentation.home.AllProductsResult
import com.multibahana.myapp.utils.ErrorType
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.io.IOException
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class SearchProductsUseCaseTest {

    private lateinit var productRepository: ProductRepository
    private lateinit var searchProductsUseCase: SearchProductsUseCase

    @Before
    fun setup() {
        productRepository = mockk()
        searchProductsUseCase = SearchProductsUseCase(productRepository)
    }

    @Test
    fun `when repository returns success, should return Success`() = runTest {
        val response = Response.success(
            ProductResponse(
                products = listOf(dummyProduct),
                total = 1,
                skip = 0,
                limit = 10
            )
        )

        coEvery {
            productRepository.searchProducts(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns response

        val result = searchProductsUseCase("test")

        assertTrue(result is AllProductsResult.Success)
    }

    @Test
    fun `when response body is null, should return Error Unknown`() = runTest {
        val response = Response.success<ProductResponse>(null)

        coEvery {
            productRepository.searchProducts(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns response

        val result = searchProductsUseCase("test")

        assertTrue(result is AllProductsResult.Error)
        val error = result as AllProductsResult.Error
        assertEquals(ErrorType.Unknown, error.errorType)
    }

    @Test
    fun `when client error, should return Client Error`() = runTest {
        val response = Response.error<ProductResponse>(
            404,
            "Not Found".toResponseBody("application/json".toMediaType())
        )

        coEvery {
            productRepository.searchProducts(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns response

        val result = searchProductsUseCase("test")

        assertTrue(result is AllProductsResult.Error)
        val error = result as AllProductsResult.Error
        assertEquals(ErrorType.Client, error.errorType)
    }

    @Test
    fun `when server error, should return Server Error`() = runTest {
        val response = Response.error<ProductResponse>(
            500,
            "Internal Server Error".toResponseBody("application/json".toMediaType())
        )

        coEvery {
            productRepository.searchProducts(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns response

        val result = searchProductsUseCase("test")

        assertTrue(result is AllProductsResult.Error)
        val error = result as AllProductsResult.Error
        assertEquals(ErrorType.Server, error.errorType)
    }


    @Test
    fun `when IOException thrown, should return Network Error`() = runTest {
        coEvery {
            productRepository.searchProducts(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } throws IOException("No internet")

        val result = searchProductsUseCase("test")

        assertTrue(result is AllProductsResult.Error)
        val error = result as AllProductsResult.Error
        assertEquals(ErrorType.Network, error.errorType)
    }

    @Test
    fun `when generic Exception thrown, should return Unknown Error`() = runTest {
        coEvery {
            productRepository.searchProducts(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } throws RuntimeException("Unexpected")

        val result = searchProductsUseCase("test")

        assertTrue(result is AllProductsResult.Error)
        val error = result as AllProductsResult.Error
        assertEquals(ErrorType.Unknown, error.errorType)
    }
}
