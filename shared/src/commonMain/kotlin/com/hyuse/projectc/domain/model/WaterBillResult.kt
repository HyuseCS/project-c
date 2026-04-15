package com.hyuse.projectc.domain.model

import kotlinx.serialization.Serializable

/**
 * Domain model representing a saved water bill calculation.
 */
@Serializable
data class WaterBillResult(
    val id: String = "",
    val billingMonth: Int,
    val billingYear: Int,
    val previousReading: Double,
    val currentReading: Double,
    val consumption: Double, // in m³
    val ratePerCubicMeter: Double,
    val totalCost: Double,
    val currencySymbol: String,
    val timestamp: Long
)
