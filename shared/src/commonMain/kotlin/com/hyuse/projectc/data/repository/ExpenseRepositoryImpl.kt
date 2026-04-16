package com.hyuse.projectc.data.repository

import com.hyuse.projectc.domain.model.Expense
import com.hyuse.projectc.domain.model.ExpenseCategory
import com.hyuse.projectc.domain.repository.ExpenseRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.Direction
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ExpenseRepositoryImpl(
    private val firestore: FirebaseFirestore = Firebase.firestore
) : ExpenseRepository {

    private fun expensesCollection(uid: String) =
        firestore.collection("users").document(uid).collection("expenses")

    private fun customCategoriesCollection(uid: String) =
        firestore.collection("users").document(uid).collection("custom_categories")

    // Hardcoded list of system categories
    private val systemCategories = listOf(
        ExpenseCategory("food_system", "Food", true, "restaurant"),
        ExpenseCategory("transport_system", "Transport", true, "commute"),
        ExpenseCategory("utilities_system", "Utilities", true, "bolt"),
        ExpenseCategory("entertainment_system", "Entertainment", true, "movie"),
        ExpenseCategory("health_system", "Health", true, "medical_services"),
        ExpenseCategory("education_system", "Education", true, "school"),
        ExpenseCategory("other_system", "Other", true, "category")
    )

    override suspend fun addExpense(uid: String, expense: Expense): Result<Unit> {
        return try {
            val docRef = if (expense.id.isNotEmpty()) {
                expensesCollection(uid).document(expense.id)
            } else {
                expensesCollection(uid).document
            }
            val expenseWithId = expense.copy(id = docRef.id)
            docRef.set(Expense.serializer(), expenseWithId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteExpense(uid: String, expenseId: String): Result<Unit> {
        return try {
            expensesCollection(uid).document(expenseId).delete()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observeExpenses(uid: String): Flow<List<Expense>> {
        return expensesCollection(uid)
            .orderBy("timestamp", Direction.DESCENDING)
            .snapshots
            .map { snapshot ->
                snapshot.documents.map { doc -> doc.data<Expense>() }
            }
    }

    override fun getSystemCategories(): List<ExpenseCategory> {
        return systemCategories
    }

    override fun observeCustomCategories(uid: String): Flow<List<ExpenseCategory>> {
        return customCategoriesCollection(uid)
            .snapshots
            .map { snapshot ->
                snapshot.documents.map { doc -> doc.data<ExpenseCategory>() }
            }
    }

    override suspend fun saveCustomCategory(uid: String, category: ExpenseCategory): Result<Unit> {
        return try {
            val docRef = if (category.id.isNotEmpty()) {
                customCategoriesCollection(uid).document(category.id)
            } else {
                customCategoriesCollection(uid).document
            }
            val categoryWithId = category.copy(id = docRef.id)
            docRef.set(ExpenseCategory.serializer(), categoryWithId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
