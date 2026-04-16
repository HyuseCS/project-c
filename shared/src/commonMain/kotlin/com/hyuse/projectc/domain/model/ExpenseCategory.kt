package com.hyuse.projectc.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ExpenseCategory(
    val id: String,
    val name: String,
    val isSystem: Boolean,
    val icon: String? = null
)
