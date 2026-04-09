package com.hyuse.projectc.domain.usecase

import com.hyuse.projectc.domain.model.UserProfile
import com.hyuse.projectc.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case for observing real-time profile updates for a given UID.
 */
class ObserveProfileUseCase(private val repository: ProfileRepository) {
    operator fun invoke(uid: String): Flow<UserProfile?> {
        return repository.observeProfile(uid)
    }
}
