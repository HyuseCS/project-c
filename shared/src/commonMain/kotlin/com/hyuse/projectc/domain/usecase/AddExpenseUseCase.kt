package com.hyuse.projectc.domain.usecase

import com.hyuse.projectc.domain.model.Expense
import com.hyuse.projectc.domain.repository.ExpenseRepository

class AddExpenseUseCase(
    private val repository: ExpenseRepository
) {
    suspend operator fun invoke(uid: String, expense: Expense): Result<Unit> {
        return repository.addExpense(uid, expense)
    }
}
