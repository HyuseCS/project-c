package com.hyuse.projectc.ui.utilities

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.background

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
                title = { 
                    Text(
                        "UTILITIES", 
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.titleLarge,
                        letterSpacing = 2.sp
                    ) 
                },
                actions = {
                    TextButton(onClick = onBack) {
                        Text("CLOSE", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(40.dp)
        ) {
            Text(
                text = "OPERATIONAL TOOLS",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Electricity Bill Calculator
            UtilityLucidItem(
                emoji = "⚡",
                title = "Electricity",
                description = "ANALYZE METER READINGS",
                onClick = onNavigateToElectricityCalculator
            )

            // Electricity Usage Predictor
            UtilityLucidItem(
                emoji = "🔮",
                title = "Predictor",
                description = "ESTIMATE APPLIANCE LOAD",
                onClick = onNavigateToElectricityPredictor
            )

            // Water Bill Calculator
            UtilityLucidItem(
                emoji = "💧",
                title = "Water",
                description = "TRACK CONSUMPTION TRENDS",
                onClick = onNavigateToWaterCalculator
            )
        }
    }
}

@Composable
private fun UtilityLucidItem(
    emoji: String,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = emoji, fontSize = 32.sp)
        }
        Column {
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-0.5).sp
            )
            Text(
                text = description,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
