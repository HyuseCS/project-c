package com.hyuse.projectc.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyuse.projectc.domain.model.User
import com.hyuse.projectc.domain.usecase.GetCurrentUserUseCase
import com.hyuse.projectc.domain.usecase.LoginUseCase
import com.hyuse.projectc.domain.usecase.LogoutUseCase
import com.hyuse.projectc.domain.usecase.SignUpUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Represents the current state of authentication UI.
 */
sealed class AuthState {
    /** Initial state — checking if user is already logged in */
    data object Loading : AuthState()

    /** No user is signed in */
    data object Unauthenticated : AuthState()

    /** User is signed in */
    data class Authenticated(val user: User) : AuthState()

    /** An error occurred during auth operation */
    data class Error(val message: String) : AuthState()
}

/**
 * ViewModel managing authentication state and operations.
 * Shared between Login and SignUp screens.
 */
class AuthViewModel(
    private val signUpUseCase: SignUpUseCase,
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase,
    getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        // Observe auth state changes (e.g., user was already signed in)
        viewModelScope.launch {
            getCurrentUserUseCase().collect { user ->
                _authState.value = if (user != null) {
                    AuthState.Authenticated(user)
                } else {
                    AuthState.Unauthenticated
                }
            }
        }
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Email and password cannot be empty")
            return
        }
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                val user = loginUseCase(email.trim(), password)
                _authState.value = AuthState.Authenticated(user)
            } catch (e: Exception) {
                _authState.value = AuthState.Error(
                    e.message ?: "Login failed. Please try again."
                )
            }
        }
    }

    fun signUp(email: String, password: String, confirmPassword: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Email and password cannot be empty")
            return
        }
        if (password != confirmPassword) {
            _authState.value = AuthState.Error("Passwords do not match")
            return
        }
        if (password.length < 6) {
            _authState.value = AuthState.Error("Password must be at least 6 characters")
            return
        }
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                val user = signUpUseCase(email.trim(), password)
                _authState.value = AuthState.Authenticated(user)
            } catch (e: Exception) {
                _authState.value = AuthState.Error(
                    e.message ?: "Sign up failed. Please try again."
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                logoutUseCase()
                // State will be updated by the authStateChanged flow in init
            } catch (e: Exception) {
                _authState.value = AuthState.Error(
                    e.message ?: "Logout failed. Please try again."
                )
            }
        }
    }

    /** Reset error state back to unauthenticated so user can retry */
    fun clearError() {
        _authState.value = AuthState.Unauthenticated
    }
}
