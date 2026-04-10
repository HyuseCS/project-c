package com.hyuse.projectc.domain.model

import kotlinx.serialization.Serializable

/**
 * Domain model representing a saved electricity bill calculation.
 */
@Serializable
data class ElectricityBillResult(
    val id: String = "",
    val billingMonth: Int,
    val billingYear: Int,
    val previousReading: Double,
    val currentReading: Double,
    val consumption: Double,
    val ratePerKwh: Double,
    val totalCost: Double,
    val currencySymbol: String,
    val timestamp: Long
)
