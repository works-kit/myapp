package com.multibahana.myapp.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multibahana.myapp.data.local.prefs.DataStoreManager
import com.multibahana.myapp.domain.usecase.GetMeUseCase
import com.multibahana.myapp.presentation.profile.state.ProfileResult
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val getMeUseCase: GetMeUseCase
) : ViewModel() {

    private val _profileState = MutableStateFlow<ProfileResult?>(null)
    val profileState: StateFlow<ProfileResult?> = _profileState


    val accessToken: StateFlow<String?> = dataStoreManager.accessToken
        .stateIn(viewModelScope, SharingStarted.Companion.Lazily, null)

    fun getMe(token: String) {
        if (_profileState.value is ProfileResult.Success) return

        viewModelScope.launch {
            _profileState.value = ProfileResult.Loading
            val result = getMeUseCase(token)
            _profileState.value = result
        }
    }

    fun logout() {
        viewModelScope.launch {
            dataStoreManager.clearTokens()
            _profileState.value = ProfileResult.LoggedOut
        }
    }
}