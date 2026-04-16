package com.hyuse.projectc.ui.expenses

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hyuse.projectc.domain.model.Expense
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ExpensesDashboardScreen(
    state: ExpensesState,
    onNextMonth: () -> Unit,
    onPreviousMonth: () -> Unit,
    onAddExpense: () -> Unit,
    onManageCategories: () -> Unit,
    onDeleteExpense: (String) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Expenses", fontWeight = FontWeight.Bold) },
                actions = {
                    TextButton(onClick = onBack) {
                        Text("Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddExpense) {
                Text("➕")
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            when (state) {
                is ExpensesState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is ExpensesState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is ExpensesState.Success -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        MonthSelector(
                            month = state.currentMonth,
                            year = state.currentYear,
                            onPrevious = onPreviousMonth,
                            onNext = onNextMonth
                        )
                        
                        SummaryCard(total = state.totalMonthly)

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            OutlinedButton(onClick = onManageCategories) {
                                Text("⚙️ Manage Categories")
                            }
                        }

                        if (state.dailyGroups.isEmpty()) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text("No expenses for this month.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        } else {
                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                state.dailyGroups.forEach { dayGroup ->
                                    stickyHeader {
                                        DayHeader(
                                            dateString = dayGroup.dateString,
                                            dayTotal = dayGroup.dayTotal
                                        )
                                    }
                                    items(dayGroup.expenses) { expense ->
                                        ExpenseItem(
                                            expense = expense,
                                            onDelete = { onDeleteExpense(expense.id) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MonthSelector(month: Int, year: Int, onPrevious: () -> Unit, onNext: () -> Unit) {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.MONTH, month)
        set(Calendar.YEAR, year)
    }
    val monthName = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(calendar.time)

    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = onPrevious) { Text("<") }
        Text(text = monthName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        TextButton(onClick = onNext) { Text(">") }
    }
}

@Composable
fun SummaryCard(total: Double) {
    val formattedTotal = NumberFormat.getCurrencyInstance().format(total)
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(24.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Monthly Total", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
            Spacer(modifier = Modifier.height(8.dp))
            Text(formattedTotal, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
        }
    }
}

@Composable
fun DayHeader(dateString: String, dayTotal: Double) {
    val formattedTotal = NumberFormat.getCurrencyInstance().format(dayTotal)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(dateString, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(formattedTotal, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun ExpenseItem(expense: Expense, onDelete: () -> Unit) {
    val formattedAmount = NumberFormat.getCurrencyInstance().format(expense.amount)
    ListItem(
        headlineContent = { Text(expense.categoryName, fontWeight = FontWeight.SemiBold) },
        supportingContent = { if (expense.description.isNotEmpty()) Text(expense.description) },
        trailingContent = { 
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(formattedAmount, fontWeight = FontWeight.Bold)
                TextButton(onClick = onDelete) {
                    Text("🗑️")
                }
            }
        },
        colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surface)
    )
}
