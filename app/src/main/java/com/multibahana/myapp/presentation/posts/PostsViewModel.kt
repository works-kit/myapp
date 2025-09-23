package com.multibahana.myapp.presentation.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.multibahana.myapp.data.model.posts.PostDto
import com.multibahana.myapp.domain.model.AllProductsEntity
import com.multibahana.myapp.domain.model.PostEntity
import com.multibahana.myapp.domain.usecase.posts.AddPostUseCase
import com.multibahana.myapp.domain.usecase.posts.DeletePostUseCase
import com.multibahana.myapp.domain.usecase.posts.GetAllPostsUseCase
import com.multibahana.myapp.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val addPostUseCase: AddPostUseCase,
    private val getAllPostsUseCase: GetAllPostsUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _listPostsState = MutableStateFlow<ResultState<List<PostEntity>>?>(null)
    val listPostsState: StateFlow<ResultState<List<PostEntity>>?> = _listPostsState

    private val _postState = MutableStateFlow<ResultState<Void?>?>(null)
    val addPostState: StateFlow<ResultState<Void?>?> = _postState

    fun addPost(post: PostEntity) {
        val currentUserId = firebaseAuth.currentUser?.uid ?: return
        val postDto = PostDto(
            title = post.title,
            content = post.content,
            userId = currentUserId
        )

        _postState.value = ResultState.Loading

        viewModelScope.launch {
            addPostUseCase(currentUserId, postDto) {
                _postState.value = it
            }
        }
    }

    fun getAllPosts() {
        if (_listPostsState.value is ResultState.Success) return

        val currentUserId = firebaseAuth.currentUser?.uid ?: return

        _listPostsState.value = ResultState.Loading

        viewModelScope.launch {
            getAllPostsUseCase(currentUserId){
                _listPostsState.value = it
            }
        }

    }

    fun getAllPostsNewest(){
        val currentUserId = firebaseAuth.currentUser?.uid ?: return

        _listPostsState.value = ResultState.Loading

        viewModelScope.launch {
            getAllPostsUseCase(currentUserId){
                _listPostsState.value = it
            }
        }
    }

    fun deletePost(postId : String){
        val currentUserId = firebaseAuth.currentUser?.uid ?: return

        viewModelScope.launch {
            deletePostUseCase(currentUserId, postId){

            }
        }

    }

    fun clearPosts(){
        _postState.value = null
    }
}