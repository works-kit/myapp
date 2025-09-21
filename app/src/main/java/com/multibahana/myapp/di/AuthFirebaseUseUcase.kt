package com.multibahana.myapp.di

import com.multibahana.myapp.domain.repository.AuthFirebaseRepository
import com.multibahana.myapp.domain.usecase.authfirebase.CurrentUserWithFirebaseUseCase
import com.multibahana.myapp.domain.usecase.authfirebase.LoginWithFirebaseUseCase
import com.multibahana.myapp.domain.usecase.authfirebase.LogoutWithFirebaseUseCase
import com.multibahana.myapp.domain.usecase.authfirebase.RegisterWithFirebaseUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AuthFirebaseUseUcase {

    @Provides
    fun provideRegisterWithFirebase(authFirebaseRepository: AuthFirebaseRepository): RegisterWithFirebaseUseCase =
        RegisterWithFirebaseUseCase(authFirebaseRepository)

    @Provides
    fun provideLoginWithFirebase(authFirebaseRepository: AuthFirebaseRepository): LoginWithFirebaseUseCase =
        LoginWithFirebaseUseCase(authFirebaseRepository)

    @Provides
    fun provideLogoutWithFirebase(authFirebaseRepository: AuthFirebaseRepository): LogoutWithFirebaseUseCase =
        LogoutWithFirebaseUseCase(authFirebaseRepository)

    @Provides
    fun provideCurrentUserWithFirebase(authFirebaseRepository: AuthFirebaseRepository): CurrentUserWithFirebaseUseCase =
        CurrentUserWithFirebaseUseCase(authFirebaseRepository)
}