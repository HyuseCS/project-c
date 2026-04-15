package com.hyuse.projectc.ui.utilities

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Utilities Hub screen — lists available utility tools.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UtilitiesHubScreen(
    onNavigateToElectricityCalculator: () -> Unit,
    onNavigateToElectricityPredictor: () -> Unit,
    onNavigateToWaterCalculator: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Utilities", fontWeight = FontWeight.Bold) },
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
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Choose a utility",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Electricity Bill Calculator
            UtilityCard(
                emoji = "⚡",
                title = "Electricity Bill Calculator",
                subtitle = "Calculate from meter readings",
                enabled = true,
                onClick = onNavigateToElectricityCalculator
            )

            // Electricity Usage Predictor
            UtilityCard(
                emoji = "🔮",
                title = "Electricity Usage Predictor",
                subtitle = "Estimate based on appliances",
                enabled = true,
                onClick = onNavigateToElectricityPredictor
            )

            // Water Bill Calculator
            UtilityCard(
                emoji = "💧",
                title = "Water Bill Calculator",
                subtitle = "Calculate from meter readings",
                enabled = true,
                onClick = onNavigateToWaterCalculator
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UtilityCard(
    emoji: String,
    title: String,
    subtitle: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        enabled = enabled,
        colors = CardDefaults.cardColors(
            containerColor = if (enabled)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = emoji, fontSize = 32.sp)
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = if (enabled)
                        MaterialTheme.colorScheme.onPrimaryContainer
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (enabled)
                        MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }
    }
}
