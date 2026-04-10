package com.hyuse.projectc.domain.usecase

import com.hyuse.projectc.domain.model.ElectricityBillResult
import com.hyuse.projectc.domain.repository.CalculatorRepository

/**
 * Use case for retrieving electricity bill calculation history.
 * Returns results sorted by timestamp descending (most recent first).
 */
class GetElectricityBillHistoryUseCase(private val repository: CalculatorRepository) {
    suspend operator fun invoke(uid: String): List<ElectricityBillResult> {
        return repository.getElectricityBillHistory(uid)
    }
}
