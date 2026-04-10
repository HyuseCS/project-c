package com.hyuse.projectc.domain.usecase

import com.hyuse.projectc.domain.model.ElectricityBillResult

/**
 * Pure calculation use case for electricity bills.
 * No side effects — takes inputs and returns a result.
 *
 * The [timestamp] is provided by the caller (ViewModel) since
 * wall-clock time APIs differ across platforms in KMP.
 */
class CalculateElectricityBillUseCase {
    operator fun invoke(
        billingMonth: Int,
        billingYear: Int,
        previousReading: Double,
        currentReading: Double,
        ratePerKwh: Double,
        currencySymbol: String,
        timestamp: Long
    ): ElectricityBillResult {
        val consumption = currentReading - previousReading
        val totalCost = consumption * ratePerKwh
        return ElectricityBillResult(
            billingMonth = billingMonth,
            billingYear = billingYear,
            previousReading = previousReading,
            currentReading = currentReading,
            consumption = consumption,
            ratePerKwh = ratePerKwh,
            totalCost = totalCost,
            currencySymbol = currencySymbol,
            timestamp = timestamp
        )
    }
}
