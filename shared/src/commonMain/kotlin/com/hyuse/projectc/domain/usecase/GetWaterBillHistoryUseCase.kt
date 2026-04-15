package com.hyuse.projectc.domain.usecase

import com.hyuse.projectc.domain.model.WaterBillResult
import com.hyuse.projectc.domain.repository.CalculatorRepository

/**
 * Use case for retrieving water bill calculation history.
 */
class GetWaterBillHistoryUseCase(private val repository: CalculatorRepository) {
    suspend operator fun invoke(uid: String): List<WaterBillResult> {
        return repository.getWaterBillHistory(uid)
    }
}
