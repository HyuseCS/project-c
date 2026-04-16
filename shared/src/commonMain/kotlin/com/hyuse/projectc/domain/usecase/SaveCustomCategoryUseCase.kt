package com.hyuse.projectc.domain.usecase

import com.hyuse.projectc.domain.model.ExpenseCategory
import com.hyuse.projectc.domain.repository.ExpenseRepository

class SaveCustomCategoryUseCase(
    private val repository: ExpenseRepository
) {
    suspend operator fun invoke(uid: String, category: ExpenseCategory): Result<Unit> {
        return repository.saveCustomCategory(uid, category)
    }
}
