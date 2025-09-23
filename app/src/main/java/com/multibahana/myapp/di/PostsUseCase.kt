package com.multibahana.myapp.di

import com.multibahana.myapp.domain.repository.PostsRepository
import com.multibahana.myapp.domain.usecase.posts.AddPostUseCase
import com.multibahana.myapp.domain.usecase.posts.DeletePostUseCase
import com.multibahana.myapp.domain.usecase.posts.GetAllPostsUseCase
import com.multibahana.myapp.domain.usecase.posts.GetPostByIdUseCase
import com.multibahana.myapp.domain.usecase.posts.SearchPostUseCase
import com.multibahana.myapp.domain.usecase.posts.UpdatePostUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object PostsUseCase {

    @Provides
    fun provideAddPostUseCase(postsRepository: PostsRepository): AddPostUseCase =
        AddPostUseCase(postsRepository)

    @Provides
    fun provideGetAllPostsUseCase(postsRepository: PostsRepository): GetAllPostsUseCase =
        GetAllPostsUseCase(postsRepository)

    @Provides
    fun provideGetPostByIdUseCase(postsRepository: PostsRepository) : GetPostByIdUseCase =
        GetPostByIdUseCase(postsRepository)

    @Provides
    fun provideSearchPostUseCase(postsRepository: PostsRepository): SearchPostUseCase =
        SearchPostUseCase(postsRepository)

    @Provides
    fun provideDeletePostUseCase(postsRepository: PostsRepository): DeletePostUseCase =
        DeletePostUseCase(postsRepository)

    @Provides
    fun provideUpdatePostUseCase(postsRepository: PostsRepository): UpdatePostUseCase =
        UpdatePostUseCase(postsRepository)


}