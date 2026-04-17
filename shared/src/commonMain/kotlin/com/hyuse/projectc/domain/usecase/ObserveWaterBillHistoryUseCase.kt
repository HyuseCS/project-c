package com.hyuse.projectc.domain.usecase

import com.hyuse.projectc.domain.model.WaterBillResult
import com.hyuse.projectc.domain.repository.CalculatorRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case to observe water bill history in real-time.
 */
class ObserveWaterBillHistoryUseCase(
    private val calculatorRepository: CalculatorRepository
) {
    operator fun invoke(uid: String): Flow<List<WaterBillResult>> {
        return calculatorRepository.observeWaterBillHistory(uid)
    }
}
