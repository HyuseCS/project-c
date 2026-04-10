package com.hyuse.projectc.domain.usecase

import com.hyuse.projectc.domain.model.ElectricityBillResult
import com.hyuse.projectc.domain.repository.CalculatorRepository

/**
 * Use case for saving an electricity bill calculation to Firestore.
 */
class SaveElectricityBillUseCase(private val repository: CalculatorRepository) {
    suspend operator fun invoke(uid: String, result: ElectricityBillResult) {
        repository.saveElectricityBill(uid, result)
    }
}
