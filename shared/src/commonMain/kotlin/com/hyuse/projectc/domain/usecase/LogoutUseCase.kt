package com.hyuse.projectc.domain.usecase

import com.hyuse.projectc.domain.repository.AuthRepository

/**
 * Use case for signing out the current user.
 */
class LogoutUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke() {
        authRepository.logout()
    }
}
