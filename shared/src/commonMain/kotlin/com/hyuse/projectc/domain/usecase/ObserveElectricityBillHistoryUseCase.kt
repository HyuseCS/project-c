package com.hyuse.projectc.domain.usecase

import com.hyuse.projectc.domain.model.ElectricityBillResult
import com.hyuse.projectc.domain.repository.CalculatorRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case to observe electricity bill history in real-time.
 */
class ObserveElectricityBillHistoryUseCase(
    private val calculatorRepository: CalculatorRepository
) {
    operator fun invoke(uid: String): Flow<List<ElectricityBillResult>> {
        return calculatorRepository.observeElectricityBillHistory(uid)
    }
}
