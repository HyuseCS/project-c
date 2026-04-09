package com.hyuse.projectc.domain.model

import kotlinx.serialization.Serializable

/**
 * Domain model representing an authenticated user.
 * This is decoupled from Firebase's internal user representation.
 */
@Serializable
data class User(
    val uid: String,
    val email: String,
    val displayName: String? = null
)
