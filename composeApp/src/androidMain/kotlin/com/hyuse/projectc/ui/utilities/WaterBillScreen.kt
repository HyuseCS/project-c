package com.hyuse.projectc.ui.utilities

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hyuse.projectc.domain.model.WaterBillResult
import org.koin.compose.viewmodel.koinViewModel
import java.util.Calendar

/**
 * Water Bill Calculator screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaterBillScreen(
    onNavigateBack: () -> Unit,
    onNavigateToHistory: () -> Unit,
    uid: String,
    viewModel: WaterBillViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val history by viewModel.history.collectAsState()

    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1 // 1-indexed

    var billingMonth by remember { mutableStateOf(currentMonth) }
    var billingYear by remember { mutableStateOf(currentYear) }
    var previousReading by remember { mutableStateOf("") }
    var currentReading by remember { mutableStateOf("") }
    var ratePerCubicMeter by remember { mutableStateOf("") }
    var currencySymbol by remember { mutableStateOf("") }

    val months = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    val years = (currentYear - 5..currentYear + 1).toList().reversed()

    var showMonthMenu by remember { mutableStateOf(false) }
    var showYearMenu by remember { mutableStateOf(false) }
    var showCurrencyMenu by remember { mutableStateOf(false) }

    // Initial load
    LaunchedEffect(uid) {
        viewModel.loadHistory(uid)
    }

    // Auto-fill defaults when history or selections change
    LaunchedEffect(history, billingMonth, billingYear) {
        if (ratePerCubicMeter.isEmpty()) {
            viewModel.getLastRate()?.let { ratePerCubicMeter = it.toString() }
        }
        if (currencySymbol.isEmpty()) {
            currencySymbol = viewModel.getLastCurrency()
        }
        
        // Auto-fill previous reading based on history
        val predictedPrev = viewModel.getPreviousReadingFor(billingMonth, billingYear)
        if (predictedPrev.isNotEmpty()) {
            previousReading = predictedPrev
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Water Bill", fontWeight = FontWeight.Bold) },
                actions = {
                    TextButton(onClick = onNavigateBack) {
                        Text("Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Inputs Section
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("Billing Period", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Month Dropdown
                            ExposedDropdownMenuBox(
                                expanded = showMonthMenu,
                                onExpandedChange = { showMonthMenu = it },
                                modifier = Modifier.weight(1f)
                            ) {
                                OutlinedTextField(
                                    value = months[billingMonth - 1],
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Month") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showMonthMenu) },
                                    modifier = Modifier.menuAnchor()
                                )
                                ExposedDropdownMenu(
                                    expanded = showMonthMenu,
                                    onDismissRequest = { showMonthMenu = false }
                                ) {
                                    months.forEachIndexed { index, month ->
                                        DropdownMenuItem(
                                            text = { Text(month) },
                                            onClick = {
                                                billingMonth = index + 1
                                                showMonthMenu = false
                                            }
                                        )
                                    }
                                }
                            }

                            // Year Dropdown
                            ExposedDropdownMenuBox(
                                expanded = showYearMenu,
                                onExpandedChange = { showYearMenu = it },
                                modifier = Modifier.weight(1f)
                            ) {
                                OutlinedTextField(
                                    value = billingYear.toString(),
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Year") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showYearMenu) },
                                    modifier = Modifier.menuAnchor()
                                )
                                ExposedDropdownMenu(
                                    expanded = showYearMenu,
                                    onDismissRequest = { showYearMenu = false }
                                ) {
                                    years.forEach { year ->
                                        DropdownMenuItem(
                                            text = { Text(year.toString()) },
                                            onClick = {
                                                billingYear = year
                                                showYearMenu = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Divider(color = MaterialTheme.colorScheme.outlineVariant)

                        Text("Readings (m³)", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)

                        OutlinedTextField(
                            value = previousReading,
                            onValueChange = { previousReading = it },
                            label = { Text("Previous Reading") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )

                        OutlinedTextField(
                            value = currentReading,
                            onValueChange = { currentReading = it },
                            label = { Text("Current Reading") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )

                        Divider(color = MaterialTheme.colorScheme.outlineVariant)

                        Text("Pricing", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ExposedDropdownMenuBox(
                                expanded = showCurrencyMenu,
                                onExpandedChange = { showCurrencyMenu = it },
                                modifier = Modifier.weight(0.4f)
                            ) {
                                OutlinedTextField(
                                    value = currencySymbol,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Curr") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showCurrencyMenu) },
                                    modifier = Modifier.menuAnchor()
                                )
                                ExposedDropdownMenu(
                                    expanded = showCurrencyMenu,
                                    onDismissRequest = { showCurrencyMenu = false }
                                ) {
                                    viewModel.currencies.forEach { (label, symbol) ->
                                        DropdownMenuItem(
                                            text = { Text(label) },
                                            onClick = {
                                                currencySymbol = symbol
                                                showCurrencyMenu = false
                                            }
                                        )
                                    }
                                }
                            }

                            OutlinedTextField(
                                value = ratePerCubicMeter,
                                onValueChange = { ratePerCubicMeter = it },
                                label = { Text("Rate / m³") },
                                modifier = Modifier.weight(0.6f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                        }

                        Button(
                            onClick = {
                                viewModel.calculate(
                                    billingMonth, billingYear, previousReading, currentReading, ratePerCubicMeter, currencySymbol
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text("Calculate Bill")
                        }
                    }
                }
            }

            // 2. Result Section
            item {
                when (val state = uiState) {
                    is WaterBillUiState.Calculated -> {
                        ResultCard(result = state.result)
                        Spacer(Modifier.height(8.dp))
                        Button(
                            onClick = { viewModel.saveResult(uid) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                        ) {
                            Text("Save to History")
                        }
                    }
                    is WaterBillUiState.Saving -> {
                        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    is WaterBillUiState.Error -> {
                        Text(state.message, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(8.dp))
                    }
                    is WaterBillUiState.SaveSuccess -> {
                        Text("Successfully saved!", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold, modifier = Modifier.padding(8.dp))
                        LaunchedEffect(Unit) {
                            kotlinx.coroutines.delay(2000)
                            viewModel.resetState()
                        }
                    }
                    is WaterBillUiState.ConfirmOverwrite -> {
                        AlertDialog(
                            onDismissRequest = { viewModel.resetState() },
                            title = { Text("Confirm Overwrite") },
                            text = { Text("You already have a water bill saved for ${months[state.newResult.billingMonth - 1]} ${state.newResult.billingYear}. Do you want to overwrite it?") },
                            confirmButton = {
                                TextButton(onClick = { viewModel.saveResult(uid, forceOverwrite = true, overwriteId = state.existingResultId) }) {
                                    Text("Overwrite")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { viewModel.resetState() }) {
                                    Text("Cancel")
                                }
                            }
                        )
                    }
                    else -> {}
                }
            }

            // 3. History Section Title
            if (history.isNotEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Recent History", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        TextButton(onClick = onNavigateToHistory) {
                            Text("View All")
                        }
                    }
                }

                items(history.take(3)) { result ->
                    HistoryItem(result, "${months[result.billingMonth - 1]} ${result.billingYear}")
                }
            }
        }
    }
}

@Composable
private fun ResultCard(result: WaterBillResult) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Calculation Result", style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "${result.currencySymbol}${String.format("%.2f", result.totalCost)}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text("Total for ${result.consumption} m³", style = MaterialTheme.typography.bodySmall)
                }
                Text("💧", fontSize = 48.sp)
            }
        }
    }
}

@Composable
private fun HistoryItem(result: WaterBillResult, periodText: String) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text("💧 $periodText", fontWeight = FontWeight.Bold)
                Text("${result.consumption} m³ @ ${result.currencySymbol}${result.ratePerCubicMeter}/m³", style = MaterialTheme.typography.bodySmall)
            }
            Text(
                "${result.currencySymbol}${String.format("%.2f", result.totalCost)}",
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 18.sp
            )
        }
    }
}
