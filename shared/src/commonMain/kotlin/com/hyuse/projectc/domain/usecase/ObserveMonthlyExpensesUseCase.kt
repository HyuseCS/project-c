package com.hyuse.projectc.domain.usecase

import com.hyuse.projectc.domain.model.Expense
import com.hyuse.projectc.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ObserveMonthlyExpensesUseCase(
    private val repository: ExpenseRepository
) {
    operator fun invoke(uid: String, startTime: Long, endTime: Long): Flow<List<Expense>> {
        return repository.observeExpenses(uid).map { expenses ->
            expenses.filter { it.timestamp in startTime..endTime }
        }
    }
}
