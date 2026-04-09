package com.hyuse.projectc.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyuse.projectc.domain.model.UserProfile
import com.hyuse.projectc.domain.usecase.GetProfileUseCase
import com.hyuse.projectc.domain.usecase.SaveProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Represents the state of the profile UI.
 */
sealed class ProfileState {
    /** Initial state or when profile is not yet loaded */
    data object Idle : ProfileState()

    /** Operation in progress */
    data object Loading : ProfileState()

    /** Profile successfully loaded */
    data class Success(val profile: UserProfile) : ProfileState()

    /** An error occurred */
    data class Error(val message: String) : ProfileState()

    /** Profile saved successfully */
    data object SaveSuccess : ProfileState()
}

/**
 * ViewModel for managing user profile creation and editing.
 */
class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val saveProfileUseCase: SaveProfileUseCase
) : ViewModel() {

    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Idle)
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    /**
     * Loads the profile for the given [uid].
     */
    fun loadProfile(uid: String) {
        _profileState.value = ProfileState.Loading
        viewModelScope.launch {
            try {
                val profile = getProfileUseCase(uid)
                if (profile != null) {
                    _profileState.value = ProfileState.Success(profile)
                } else {
                    _profileState.value = ProfileState.Idle
                }
            } catch (e: Exception) {
                _profileState.value = ProfileState.Error(e.message ?: "Failed to load profile")
            }
        }
    }

    /**
     * Saves or updates the user profile.
     */
    fun saveProfile(uid: String, name: String, email: String, university: String, course: String) {
        if (name.isBlank() || university.isBlank() || course.isBlank()) {
            _profileState.value = ProfileState.Error("All fields are required")
            return
        }

        _profileState.value = ProfileState.Loading
        viewModelScope.launch {
            try {
                val profile = UserProfile(
                    uid = uid,
                    name = name.trim(),
                    email = email.trim(),
                    university = university.trim(),
                    course = course.trim()
                )
                saveProfileUseCase(profile)
                _profileState.value = ProfileState.SaveSuccess
            } catch (e: Exception) {
                _profileState.value = ProfileState.Error(e.message ?: "Failed to save profile")
            }
        }
    }

    /**
     * Resets the state back to Idle or Success if we have a profile.
     */
    fun clearError() {
        // Simple reset, could be more sophisticated
        if (_profileState.value is ProfileState.Error) {
            _profileState.value = ProfileState.Idle
        }
    }
}
