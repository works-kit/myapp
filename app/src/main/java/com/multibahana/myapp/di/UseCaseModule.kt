package com.multibahana.myapp.di

import com.multibahana.myapp.data.local.prefs.DataStoreManager
import com.multibahana.myapp.domain.repository.AuthRepository
import com.multibahana.myapp.domain.usecase.LoginUseCase
import com.multibahana.myapp.domain.usecase.GetMeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideLoginUseCase(
        authRepository: AuthRepository,
        dataStoreManager: DataStoreManager
    ): LoginUseCase = LoginUseCase(authRepository, dataStoreManager)

    @Provides
    fun provideProfileUseCase(
        authRepository: AuthRepository,
        dataStoreManager: DataStoreManager
    ): GetMeUseCase = GetMeUseCase(authRepository, dataStoreManager)

}