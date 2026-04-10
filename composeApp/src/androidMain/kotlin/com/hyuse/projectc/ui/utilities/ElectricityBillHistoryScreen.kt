package com.hyuse.projectc.ui.utilities

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
import com.hyuse.projectc.domain.model.ElectricityBillResult
import java.util.Locale

/**
 * Screen dedicated to showing the full electricity bill history.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElectricityBillHistoryScreen(
    history: List<ElectricityBillResult>,
    onBack: () -> Unit
) {
    val months = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bill History", fontWeight = FontWeight.Bold) },
                actions = {
                    TextButton(onClick = onBack) {
                        Text("Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (history.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No history available.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(history) { item ->
                    val monthName = months[item.billingMonth - 1]
                    HistoryCard(item, "$monthName ${item.billingYear}")
                }
            }
        }
    }
}

@Composable
private fun HistoryCard(result: ElectricityBillResult, periodText: String) {
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
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${formatNumber(result.consumption)} kWh",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Previous: ${formatNumber(result.previousReading)} · Current: ${formatNumber(result.currentReading)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )
            }
            Text(
                text = "${result.currencySymbol}${formatNumber(result.totalCost)}",
                style = MaterialTheme.typography.titleLarge,
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
