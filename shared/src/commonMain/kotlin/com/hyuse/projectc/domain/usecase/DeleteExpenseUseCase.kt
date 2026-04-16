package com.hyuse.projectc.domain.usecase

import com.hyuse.projectc.domain.repository.ExpenseRepository

class DeleteExpenseUseCase(
    private val repository: ExpenseRepository
) {
    suspend operator fun invoke(uid: String, expenseId: String): Result<Unit> {
        return repository.deleteExpense(uid, expenseId)
    }
}
