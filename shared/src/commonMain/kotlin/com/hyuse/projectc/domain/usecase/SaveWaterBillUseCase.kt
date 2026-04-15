package com.hyuse.projectc.domain.usecase

import com.hyuse.projectc.domain.model.WaterBillResult
import com.hyuse.projectc.domain.repository.CalculatorRepository

/**
 * Use case for saving a water bill calculation to Firestore.
 */
class SaveWaterBillUseCase(private val repository: CalculatorRepository) {
    suspend operator fun invoke(uid: String, result: WaterBillResult) {
        repository.saveWaterBill(uid, result)
    }
}
