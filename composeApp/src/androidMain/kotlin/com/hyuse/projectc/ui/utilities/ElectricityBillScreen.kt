package com.hyuse.projectc.ui.utilities

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import android.widget.Toast
import com.hyuse.projectc.domain.model.ElectricityBillResult
import org.koin.compose.viewmodel.koinViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Electricity Bill Calculator screen.
 * Calculator form on top, inline history below.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElectricityBillScreen(
    uiState: ElectricBillUiState,
    history: List<ElectricityBillResult>,
    lastRate: Double?,
    lastCurrency: String,
    onCalculate: (month: Int, year: Int, previousReading: String, currentReading: String, rate: String, currency: String) -> Unit,
    onSave: (forceOverwrite: Boolean, overwriteId: String?) -> Unit,
    onNavigateToHistory: () -> Unit,
    onResetState: () -> Unit,
    onBack: () -> Unit,
    viewModel: ElectricityBillViewModel = koinViewModel()
) {
    val currentCalendar = Calendar.getInstance()
    var selectedMonth by remember { mutableStateOf(currentCalendar.get(Calendar.MONTH) + 1) } // 1-12
    var selectedYear by remember { mutableStateOf(currentCalendar.get(Calendar.YEAR)) }

    var previousReading by remember { mutableStateOf("") }
    var currentReading by remember { mutableStateOf("") }
    var ratePerKwh by remember { mutableStateOf(lastRate?.toString() ?: "") }
    var currencySymbol by remember { mutableStateOf(lastCurrency) }

    val months = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )
    val context = LocalContext.current

    // Pre-fill previous reading when month/year changes or history loads
    LaunchedEffect(selectedMonth, selectedYear, history) {
        val prev = viewModel.getPreviousReadingFor(selectedMonth, selectedYear)
        if (prev.isNotEmpty()) previousReading = prev
    }

    // Auto-fill rate and currency on initial load when history becomes available
    LaunchedEffect(history) {
        if (history.isNotEmpty() && ratePerKwh.isEmpty()) {
            val mostRecent = history.first()
            ratePerKwh = mostRecent.ratePerKwh.toString()
            currencySymbol = mostRecent.currencySymbol
        }
    }

    // Show save success message
    LaunchedEffect(uiState) {
        if (uiState is ElectricBillUiState.SaveSuccess) {
            // Clear the form after successful save
            previousReading = ""
            currentReading = ""
            Toast.makeText(context, "Calculation saved to history!", Toast.LENGTH_SHORT).show()
            viewModel.resetState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Electricity Bill", fontWeight = FontWeight.Bold) },
                actions = {
                    TextButton(onClick = onBack) {
                        Text("Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- Billing Period Selection ---
            Text(
                text = "Billing Period",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                var monthExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = monthExpanded,
                    onExpandedChange = { monthExpanded = !monthExpanded },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        readOnly = true,
                        value = months[selectedMonth - 1],
                        onValueChange = {},
                        label = { Text("Month") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = monthExpanded) },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = monthExpanded,
                        onDismissRequest = { monthExpanded = false }
                    ) {
                        months.forEachIndexed { index, selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption) },
                                onClick = {
                                    selectedMonth = index + 1
                                    monthExpanded = false
                                }
                            )
                        }
                    }
                }

                var yearExpanded by remember { mutableStateOf(false) }
                val startYear = 2024
                val years = (startYear..currentCalendar.get(Calendar.YEAR) + 1).toList()
                
                ExposedDropdownMenuBox(
                    expanded = yearExpanded,
                    onExpandedChange = { yearExpanded = !yearExpanded },
                    modifier = Modifier.width(120.dp)
                ) {
                    OutlinedTextField(
                        readOnly = true,
                        value = selectedYear.toString(),
                        onValueChange = {},
                        label = { Text("Year") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = yearExpanded) },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = yearExpanded,
                        onDismissRequest = { yearExpanded = false }
                    ) {
                        years.forEach { yearOpt ->
                            DropdownMenuItem(
                                text = { Text(yearOpt.toString()) },
                                onClick = {
                                    selectedYear = yearOpt
                                    yearExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            // --- Calculator Form ---
            Text(
                text = "Enter your meter readings",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedTextField(
                value = previousReading,
                onValueChange = { previousReading = it },
                label = { Text("Previous Reading (kWh)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                placeholder = { Text("e.g. 4520") }
            )

            OutlinedTextField(
                value = currentReading,
                onValueChange = { currentReading = it },
                label = { Text("Current Reading (kWh)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                placeholder = { Text("e.g. 4680") }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                var currencyExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = currencyExpanded,
                    onExpandedChange = { currencyExpanded = !currencyExpanded },
                    modifier = Modifier.width(120.dp)
                ) {
                    OutlinedTextField(
                        readOnly = true,
                        value = currencySymbol,
                        onValueChange = {},
                        label = { Text("Currency") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = currencyExpanded) },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = currencyExpanded,
                        onDismissRequest = { currencyExpanded = false }
                    ) {
                        viewModel.currencies.forEach { (name, sym) ->
                            DropdownMenuItem(
                                text = { Text(name) },
                                onClick = {
                                    currencySymbol = sym
                                    currencyExpanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = ratePerKwh,
                    onValueChange = { ratePerKwh = it },
                    label = { Text("Rate per kWh") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    placeholder = { Text("e.g. 11.50") }
                )
            }

            Button(
                onClick = {
                    onCalculate(selectedMonth, selectedYear, previousReading, currentReading, ratePerKwh, currencySymbol)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                enabled = uiState !is ElectricBillUiState.Saving
            ) {
                Text("Calculate")
            }

            // --- Result Card ---
            if (uiState is ElectricBillUiState.Calculated || uiState is ElectricBillUiState.ConfirmOverwrite) {
                val result = if (uiState is ElectricBillUiState.Calculated) uiState.result else (uiState as ElectricBillUiState.ConfirmOverwrite).newResult
                ResultCard(result = result)
                Button(
                    onClick = { onSave(false, null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary
                    )
                ) {
                    Text("Save to History")
                }
            }

            if (uiState is ElectricBillUiState.Saving) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(modifier = Modifier.size(32.dp))
                }
            }

            // --- Error & Confirmation Dialogs ---
            if (uiState is ElectricBillUiState.Error) {
                AlertDialog(
                    onDismissRequest = onResetState,
                    title = { Text("Error") },
                    text = { Text(uiState.message) },
                    confirmButton = {
                        TextButton(onClick = onResetState) {
                            Text("OK")
                        }
                    }
                )
            }

            if (uiState is ElectricBillUiState.ConfirmOverwrite) {
                AlertDialog(
                    onDismissRequest = onResetState,
                    title = { Text("Existing Bill Found") },
                    text = { Text("You already have an electricity bill saved for ${months[uiState.newResult.billingMonth - 1]} ${uiState.newResult.billingYear}. Do you want to overwrite it?") },
                    confirmButton = {
                        TextButton(onClick = { onSave(true, uiState.existingResultId) }) {
                            Text("Overwrite")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = onResetState) {
                            Text("Cancel")
                        }
                    }
                )
            }

            // --- History Section (Latest Only) ---
            if (history.isNotEmpty()) {
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                Text(
                    text = "📋 Latest History",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                val latest = history.first()
                val monthName = months[latest.billingMonth - 1].take(3)
                HistoryItem(latest, "$monthName ${latest.billingYear}")

                Spacer(modifier = Modifier.height(4.dp))

                OutlinedButton(
                    onClick = onNavigateToHistory,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("View Full History")
                }
            }
        }
    }
}

@Composable
private fun ResultCard(result: ElectricityBillResult) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Result",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Consumption",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
                Text(
                    text = "${formatNumber(result.consumption)} kWh",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Estimated Bill",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
                Text(
                    text = "${result.currencySymbol}${formatNumber(result.totalCost)}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
private fun HistoryItem(result: ElectricityBillResult, periodText: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "⚡ $periodText",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${formatNumber(result.consumption)} kWh",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }
            Text(
                text = "${result.currencySymbol}${formatNumber(result.totalCost)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

/**
 * Formats a Double to 2 decimal places with comma separators.
 */
private fun formatNumber(value: Double): String {
    return String.format(Locale.getDefault(), "%,.2f", value)
}
