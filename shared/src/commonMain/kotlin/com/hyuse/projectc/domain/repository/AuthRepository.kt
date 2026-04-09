package com.hyuse.projectc.domain.repository

import com.hyuse.projectc.domain.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for authentication operations.
 * Implemented by AuthRepositoryImpl in the data layer using Firebase Auth.
 */
interface AuthRepository {
    /** Emits the current user whenever auth state changes (null = signed out) */
    val currentUser: Flow<User?>

    /** Create a new account with email and password */
    suspend fun signUp(email: String, password: String): User

    /** Sign in with existing email and password */
    suspend fun login(email: String, password: String): User

    /** Sign out the current user */
    suspend fun logout()
}
