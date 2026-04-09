package com.hyuse.projectc.domain.usecase

import com.hyuse.projectc.domain.model.User
import com.hyuse.projectc.domain.repository.AuthRepository

/**
 * Use case for signing in an existing user.
 */
class LoginUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): User {
        return authRepository.login(email, password)
    }
}
