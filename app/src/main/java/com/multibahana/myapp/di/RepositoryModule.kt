package com.multibahana.myapp.di

import com.multibahana.myapp.data.remote.api.AuthService
import com.multibahana.myapp.data.remote.api.ProductService
import com.multibahana.myapp.data.repository.AuthRepositoryImpl
import com.multibahana.myapp.data.repository.ProductRepositoryImpl
import com.multibahana.myapp.domain.repository.AuthRepository
import com.multibahana.myapp.domain.repository.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideAuthRepository(api: AuthService): AuthRepository =
        AuthRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideProductRepository(
        productService: ProductService
    ): ProductRepository =
        ProductRepositoryImpl(productService)
}