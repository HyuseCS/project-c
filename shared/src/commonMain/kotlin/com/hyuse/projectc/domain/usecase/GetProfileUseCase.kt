package com.hyuse.projectc.domain.usecase

import com.hyuse.projectc.domain.model.UserProfile
import com.hyuse.projectc.domain.repository.ProfileRepository

/**
 * Use case for fetching a user's profile for a given UID.
 */
class GetProfileUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(uid: String): UserProfile? {
        return repository.getProfile(uid)
    }
}
