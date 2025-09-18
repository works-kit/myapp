package com.multibahana.myapp

import com.multibahana.myapp.data.model.product.ProductResponse
import com.multibahana.myapp.domain.repository.ProductRepository
import com.multibahana.myapp.domain.usecase.product.GetAllProductsUseCase
import com.multibahana.myapp.presentation.home.AllProductsResult
import com.multibahana.myapp.utils.ErrorType
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.io.IOException
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class GetAllProductsUseCaseTest {

    private lateinit var productRepository: ProductRepository
    private lateinit var getAllProductsUseCase: GetAllProductsUseCase

    @Before
    fun setup() {
        productRepository = mockk()
        getAllProductsUseCase = GetAllProductsUseCase(productRepository)
    }

    @Test
    fun `when repository returns success, should return Success`() = runTest {
        // Given
        val mockResponse = mockResponse
        coEvery {
            productRepository.getProducts(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns mockResponse

        // When
        val result = getAllProductsUseCase()

        // Then
        assertTrue(result is AllProductsResult.Success)
    }

    @Test
    fun `when repository returns client error, should return Error with Client type`() = runTest {
        // Given
        val errorBody =
            """{"error":"Invalid input"}""".toResponseBody("application/json".toMediaTypeOrNull())
        val mockResponse = Response.error<ProductResponse>(400, errorBody)
        coEvery {
            productRepository.getProducts(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns mockResponse

        // When
        val result = getAllProductsUseCase()

        // Then
        assertTrue(result is AllProductsResult.Error)
        val error = result as AllProductsResult.Error
        assertEquals(ErrorType.Client, error.errorType)
    }

    @Test
    fun `when repository throws IOException, should return Network error`() = runTest {
        // Given
        coEvery {
            productRepository.getProducts(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } throws IOException("No internet")

        // When
        val result = getAllProductsUseCase()

        // Then
        assertTrue(result is AllProductsResult.Error)
        val error = result as AllProductsResult.Error
        assertEquals(ErrorType.Network, error.errorType)
    }

}

