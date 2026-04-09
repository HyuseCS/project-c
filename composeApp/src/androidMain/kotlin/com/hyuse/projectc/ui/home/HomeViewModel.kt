package com.hyuse.projectc.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyuse.projectc.domain.usecase.GetProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * UI State for the Home screen.
 */
sealed class HomeState {
    /** Initial state — checking profile */
    data object Loading : HomeState()

    /** User profile does not exist in Firestore */
    data object ProfileMissing : HomeState()

    /** User profile exists */
    data object ProfileExists : HomeState()

    /** An error occurred while checking profile */
    data class Error(val message: String) : HomeState()
}

/**
 * ViewModel for the Home screen.
 * Responsible for checking if the user has completed their profile.
 */
class HomeViewModel(
    private val getProfileUseCase: GetProfileUseCase
) : ViewModel() {

    private val _homeState = MutableStateFlow<HomeState>(HomeState.Loading)
    val homeState: StateFlow<HomeState> = _homeState.asStateFlow()

    /**
     * Checks if a profile exists for the given [uid].
     */
    fun checkProfile(uid: String) {
        _homeState.value = HomeState.Loading
        viewModelScope.launch {
            try {
                val profile = getProfileUseCase(uid)
                if (profile == null) {
                    _homeState.value = HomeState.ProfileMissing
                } else {
                    _homeState.value = HomeState.ProfileExists
                }
            } catch (e: Exception) {
                _homeState.value = HomeState.Error(e.message ?: "Failed to check profile")
            }
        }
    }
}
