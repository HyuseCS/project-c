package com.hyuse.projectc.domain.usecase

import com.hyuse.projectc.domain.repository.CalculatorRepository

class DeleteElectricityBillUseCase(
    private val repository: CalculatorRepository
) {
    suspend operator fun invoke(uid: String, resultId: String) {
        repository.deleteElectricityBill(uid, resultId)
    }
}
