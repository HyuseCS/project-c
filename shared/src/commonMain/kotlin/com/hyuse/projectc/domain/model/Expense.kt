package com.hyuse.projectc.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Expense(
    val id: String = "",
    val amount: Double,
    val categoryId: String,
    val categoryName: String,
    val description: String = "",
    val timestamp: Long
)
