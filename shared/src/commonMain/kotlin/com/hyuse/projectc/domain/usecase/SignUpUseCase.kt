package com.hyuse.projectc.domain.usecase

import com.hyuse.projectc.domain.model.User
import com.hyuse.projectc.domain.repository.AuthRepository

/**
 * Use case for creating a new user account.
 */
class SignUpUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): User {
        return authRepository.signUp(email, password)
    }
}
