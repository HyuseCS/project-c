package com.hyuse.projectc.domain.usecase

import com.hyuse.projectc.domain.model.ExpenseCategory
import com.hyuse.projectc.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetMergedCategoriesUseCase(
    private val repository: ExpenseRepository
) {
    operator fun invoke(uid: String): Flow<List<ExpenseCategory>> {
        val systemCategories = repository.getSystemCategories()
        return repository.observeCustomCategories(uid).map { customCategories ->
            systemCategories + customCategories
        }
    }
}
