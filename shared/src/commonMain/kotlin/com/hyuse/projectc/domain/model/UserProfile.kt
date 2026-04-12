package com.hyuse.projectc.domain.model

import kotlinx.serialization.Serializable

/**
 * Domain model representing a user's extended profile stored in Firestore.
 */
@Serializable
data class UserProfile(
    val uid: String,
    val name: String,
    val nickname: String? = null,
    val email: String,
    val university: String,
    val course: String
)
