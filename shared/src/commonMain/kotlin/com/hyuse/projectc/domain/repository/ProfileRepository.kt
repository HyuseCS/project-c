package com.hyuse.projectc.domain.repository

import com.hyuse.projectc.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining operations for managing user profiles in Firestore.
 */
interface ProfileRepository {
    /**
     * Fetches the user profile for a given [uid] once.
     */
    suspend fun getProfile(uid: String): UserProfile?

    /**
     * Saves or updates a [UserProfile].
     */
    suspend fun saveProfile(profile: UserProfile)

    /**
     * Returns a [Flow] of real-time updates for a user's profile.
     */
    fun observeProfile(uid: String): Flow<UserProfile?>
}
