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
import androidx.compose.ui.unit.sp
import com.hyuse.projectc.domain.model.AppConstants
import com.hyuse.projectc.domain.model.Expense
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

import com.hyuse.projectc.ui.theme.LucidSurface

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ExpensesDashboardScreen(
    state: ExpensesState,
    onNextMonth: () -> Unit,
    onPreviousMonth: () -> Unit,
    onAddExpense: () -> Unit,
    onManageCategories: () -> Unit,
    onDeleteExpense: (String) -> Unit,
    onBack: () -> Unit,
    showBackButton: Boolean = true
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "EXPENSES", 
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.titleLarge,
                        letterSpacing = 2.sp
                    ) 
                },
                actions = {
                    if (showBackButton) {
                        TextButton(onClick = onBack) {
                            Text("CLOSE", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddExpense,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.padding(bottom = 100.dp, end = 16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Expense")
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            when (state) {
                is ExpensesState.Loading -> {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter))
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
                        MonthSelectorLucid(
                            month = state.currentMonth,
                            year = state.currentYear,
                            onPrevious = onPreviousMonth,
                            onNext = onNextMonth
                        )
                        
                        SummaryHero(total = state.totalMonthly, currencySymbol = state.currencySymbol)

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 24.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "TRANSACTION LOG",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            TextButton(onClick = onManageCategories) {
                                Text("CATEGORIES", style = MaterialTheme.typography.labelLarge)
                            }
                        }

                        if (state.dailyGroups.isEmpty()) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text("No transactions recorded.", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f))
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(bottom = 120.dp)
                            ) {
                                state.dailyGroups.forEach { dayGroup ->
                                    stickyHeader {
                                        LucidDayHeader(
                                            dateString = dayGroup.dateString,
                                            dayTotal = dayGroup.dayTotal,
                                            currencySymbol = state.currencySymbol
                                        )
                                    }
                                    items(dayGroup.expenses) { expense ->
                                        LucidExpenseItem(
                                            expense = expense,
                                            currencySymbol = state.currencySymbol,
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
fun MonthSelectorLucid(month: Int, year: Int, onPrevious: () -> Unit, onNext: () -> Unit) {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.MONTH, month)
        set(Calendar.YEAR, year)
    }
    val monthName = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(calendar.time).uppercase()

    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPrevious) { 
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Month") 
        }
        Text(
            text = monthName, 
            style = MaterialTheme.typography.titleLarge, 
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp
        )
        IconButton(onClick = onNext) { 
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Month") 
        }
    }
}

@Composable
fun SummaryHero(total: Double, currencySymbol: String) {
    val formattedTotal = AppConstants.formatCurrency(total, currencySymbol)
    Box(modifier = Modifier.padding(horizontal = 24.dp)) {
        LucidSurface(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(32.dp), horizontalAlignment = Alignment.Start) {
                Text(
                    "MONTHLY TOTAL", 
                    style = MaterialTheme.typography.labelLarge, 
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    formattedTotal, 
                    style = MaterialTheme.typography.headlineLarge, 
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}

@Composable
fun LucidDayHeader(dateString: String, dayTotal: Double, currencySymbol: String) {
    val formattedTotal = AppConstants.formatCurrency(dayTotal, currencySymbol)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                dateString.uppercase(), 
                fontWeight = FontWeight.Black, 
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                formattedTotal, 
                fontWeight = FontWeight.Bold, 
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
    }
}

@Composable
fun LucidExpenseItem(expense: Expense, currencySymbol: String, onDelete: () -> Unit) {
    val formattedAmount = AppConstants.formatCurrency(expense.amount, currencySymbol)
    ListItem(
        headlineContent = { 
            Text(
                expense.categoryName.uppercase(), 
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge
            ) 
        },
        supportingContent = { 
            if (expense.description.isNotEmpty()) {
                Text(
                    expense.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        trailingContent = { 
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    formattedAmount, 
                    fontWeight = FontWeight.Black,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.width(16.dp))
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.padding(horizontal = 8.dp)
    )
}
