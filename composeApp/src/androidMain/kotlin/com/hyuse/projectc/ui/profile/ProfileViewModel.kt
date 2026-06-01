package com.hyuse.projectc.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyuse.projectc.domain.model.PhotonFeature
import com.hyuse.projectc.domain.model.UserProfile
import com.hyuse.projectc.domain.usecase.GetProfileUseCase
import com.hyuse.projectc.domain.usecase.SaveProfileUseCase
import com.hyuse.projectc.domain.usecase.SearchAddressUseCase
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
    private val saveProfileUseCase: SaveProfileUseCase,
    private val searchAddressUseCase: SearchAddressUseCase
) : ViewModel() {

    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Idle)
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    private val _searchResults = MutableStateFlow<List<PhotonFeature>>(emptyList())
    val searchResults: StateFlow<List<PhotonFeature>> = _searchResults.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

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
     * Performs a debounced search for addresses using Photon API.
     */
    fun searchAddress(query: String) {
        if (query.isBlank() || query.length < 2) {
            _searchResults.value = emptyList()
            return
        }

        viewModelScope.launch {
            _isSearching.value = true
            try {
                _searchResults.value = searchAddressUseCase(query)
            } catch (e: Exception) {
                _searchResults.value = emptyList()
            } finally {
                _isSearching.value = false
            }
        }
    }

    /**
     * Saves or updates the user profile.
     */
    fun saveProfile(
        uid: String,
        name: String,
        nickname: String,
        email: String,
        university: String,
        course: String,
        currencySymbol: String,
        address: String = "",
        homeLatitude: Double? = null,
        homeLongitude: Double? = null
    ) {
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
                    nickname = nickname.trim().takeIf { it.isNotBlank() },
                    email = email.trim(),
                    university = university.trim(),
                    course = course.trim(),
                    currencySymbol = currencySymbol,
                    address = address.trim(),
                    homeLatitude = homeLatitude,
                    homeLongitude = homeLongitude
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
