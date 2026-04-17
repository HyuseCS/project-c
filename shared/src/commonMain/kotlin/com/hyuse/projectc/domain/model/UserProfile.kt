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
    val course: String,
    val currencySymbol: String = "RM",
    val dashboardWidgets: List<String> = listOf("welcome", "electricity", "water", "expenses", "activity", "actions")
)
