package com.hyuse.projectc.domain.usecase

import com.hyuse.projectc.domain.model.User
import com.hyuse.projectc.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case for observing the currently authenticated user.
 * Emits null when no user is signed in.
 */
class GetCurrentUserUseCase(private val authRepository: AuthRepository) {
    operator fun invoke(): Flow<User?> {
        return authRepository.currentUser
    }
}
