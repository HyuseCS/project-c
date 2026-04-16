package com.hyuse.projectc.domain.repository

import com.hyuse.projectc.domain.model.Expense
import com.hyuse.projectc.domain.model.ExpenseCategory
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    suspend fun addExpense(uid: String, expense: Expense): Result<Unit>
    suspend fun deleteExpense(uid: String, expenseId: String): Result<Unit>
    fun observeExpenses(uid: String): Flow<List<Expense>>
    fun getSystemCategories(): List<ExpenseCategory>
    fun observeCustomCategories(uid: String): Flow<List<ExpenseCategory>>
    suspend fun saveCustomCategory(uid: String, category: ExpenseCategory): Result<Unit>
}
