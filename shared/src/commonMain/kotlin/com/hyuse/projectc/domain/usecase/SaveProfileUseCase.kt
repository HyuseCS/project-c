package com.hyuse.projectc.domain.usecase

import com.hyuse.projectc.domain.model.UserProfile
import com.hyuse.projectc.domain.repository.ProfileRepository

/**
 * Use case for saving or updating a user's profile.
 */
class SaveProfileUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(profile: UserProfile) {
        repository.saveProfile(profile)
    }
}
