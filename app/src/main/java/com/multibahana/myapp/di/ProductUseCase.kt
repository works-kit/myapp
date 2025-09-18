package com.multibahana.myapp.di

import com.multibahana.myapp.domain.repository.ProductRepository
import com.multibahana.myapp.domain.usecase.product.GetAllProductsUseCase
import com.multibahana.myapp.domain.usecase.product.GetProductByIdUseCase
import com.multibahana.myapp.domain.usecase.product.SearchProductsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ProductUseCase {

    @Provides
    fun provideGetAllProductsUseCase(productRepository: ProductRepository): GetAllProductsUseCase =
        GetAllProductsUseCase(productRepository)

    @Provides
    fun provideGetProductByIdUseCase(productRepository: ProductRepository): GetProductByIdUseCase {
        return GetProductByIdUseCase(productRepository)
    }

    @Provides
    fun provideSearchProductsUseCase(productRepository: ProductRepository): SearchProductsUseCase {
        return SearchProductsUseCase(productRepository)
    }
}