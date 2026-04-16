package com.hyuse.projectc.ui.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyuse.projectc.domain.model.Expense
import com.hyuse.projectc.domain.model.ExpenseCategory
import com.hyuse.projectc.domain.usecase.AddExpenseUseCase
import com.hyuse.projectc.domain.usecase.DeleteExpenseUseCase
import com.hyuse.projectc.domain.usecase.GetMergedCategoriesUseCase
import com.hyuse.projectc.domain.usecase.ObserveMonthlyExpensesUseCase
import com.hyuse.projectc.domain.usecase.SaveCustomCategoryUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class DayExpenses(
    val dateString: String,
    val dayTotal: Double,
    val expenses: List<Expense>
)

sealed class ExpensesState {
    data object Loading : ExpensesState()
    data class Success(
        val totalMonthly: Double,
        val dailyGroups: List<DayExpenses>,
        val currentMonth: Int,
        val currentYear: Int,
        val categories: List<ExpenseCategory>
    ) : ExpensesState()
    data class Error(val message: String) : ExpensesState()
}

class ExpensesViewModel(
    private val observeMonthlyExpensesUseCase: ObserveMonthlyExpensesUseCase,
    private val addExpenseUseCase: AddExpenseUseCase,
    private val deleteExpenseUseCase: DeleteExpenseUseCase,
    private val getMergedCategoriesUseCase: GetMergedCategoriesUseCase,
    private val saveCustomCategoryUseCase: SaveCustomCategoryUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<ExpensesState>(ExpensesState.Loading)
    val state = _state.asStateFlow()

    private var currentYear = Calendar.getInstance().get(Calendar.YEAR)
    private var currentMonth = Calendar.getInstance().get(Calendar.MONTH)

    private var uid: String? = null

    fun loadData(uid: String) {
        this.uid = uid
        observeData()
    }

    private fun observeData() {
        val currentUid = this.uid ?: return
        
        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, currentYear)
            set(Calendar.MONTH, currentMonth)
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val startTime = calendar.timeInMillis
        
        calendar.add(Calendar.MONTH, 1)
        val endTime = calendar.timeInMillis - 1
        
        _state.value = ExpensesState.Loading
        
        viewModelScope.launch {
            combine(
                observeMonthlyExpensesUseCase(currentUid, startTime, endTime),
                getMergedCategoriesUseCase(currentUid)
            ) { expenses, categories ->
                val totalMonthly = expenses.sumOf { it.amount }
                
                val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                val grouped = expenses.groupBy { 
                    val d = Date(it.timestamp)
                    dateFormat.format(d)
                }.map { (dateString, dayExpenses) ->
                    val dayTotal = dayExpenses.sumOf { it.amount }
                    DayExpenses(
                        dateString = dateString,
                        dayTotal = dayTotal,
                        expenses = dayExpenses.sortedByDescending { it.timestamp }
                    )
                }.sortedByDescending { 
                    it.expenses.firstOrNull()?.timestamp ?: 0L
                }

                ExpensesState.Success(
                    totalMonthly = totalMonthly,
                    dailyGroups = grouped,
                    currentMonth = currentMonth,
                    currentYear = currentYear,
                    categories = categories
                )
            }.catch { e ->
                _state.value = ExpensesState.Error(e.message ?: "Unknown error")
            }.collectLatest { successState ->
                _state.value = successState
            }
        }
    }

    fun nextMonth() {
        if (currentMonth == 11) {
            currentMonth = 0
            currentYear++
        } else {
            currentMonth++
        }
        observeData()
    }

    fun previousMonth() {
        if (currentMonth == 0) {
            currentMonth = 11
            currentYear--
        } else {
            currentMonth--
        }
        observeData()
    }

    fun addExpense(amount: Double, categoryId: String, categoryName: String, description: String) {
        val currentUid = this.uid ?: return
        viewModelScope.launch {
            val expense = Expense(
                amount = amount,
                categoryId = categoryId,
                categoryName = categoryName,
                description = description,
                timestamp = System.currentTimeMillis()
            )
            addExpenseUseCase(currentUid, expense)
        }
    }

    fun deleteExpense(expenseId: String) {
        val currentUid = this.uid ?: return
        viewModelScope.launch {
            deleteExpenseUseCase(currentUid, expenseId)
        }
    }

    fun addCustomCategory(name: String, icon: String?) {
        val currentUid = this.uid ?: return
        viewModelScope.launch {
            val category = ExpenseCategory(
                id = "",
                name = name,
                isSystem = false,
                icon = icon
            )
            saveCustomCategoryUseCase(currentUid, category)
        }
    }
}
