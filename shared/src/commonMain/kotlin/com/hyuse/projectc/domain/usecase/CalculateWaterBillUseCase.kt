package com.hyuse.projectc.domain.usecase

import com.hyuse.projectc.domain.model.WaterBillResult

/**
 * Pure calculation use case for water bills.
 */
class CalculateWaterBillUseCase {
    operator fun invoke(
        billingMonth: Int,
        billingYear: Int,
        previousReading: Double,
        currentReading: Double,
        ratePerCubicMeter: Double,
        currencySymbol: String,
        timestamp: Long
    ): WaterBillResult {
        val consumption = currentReading - previousReading
        val totalCost = consumption * ratePerCubicMeter
        return WaterBillResult(
            billingMonth = billingMonth,
            billingYear = billingYear,
            previousReading = previousReading,
            currentReading = currentReading,
            consumption = consumption,
            ratePerCubicMeter = ratePerCubicMeter,
            totalCost = totalCost,
            currencySymbol = currencySymbol,
            timestamp = timestamp
        )
    }
}
