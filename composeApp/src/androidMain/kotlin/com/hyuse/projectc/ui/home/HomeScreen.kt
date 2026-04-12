package com.hyuse.projectc.ui.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeState.Success,
    onLogout: () -> Unit,
    onNavigateToProfile: () -> Unit = {},
    onNavigateToUtilities: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Project C", fontWeight = FontWeight.Bold) },
                actions = { TextButton(onClick = onLogout) { Text("Logout") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            contentPadding = PaddingValues(vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                WelcomeCard(name = state.nickname ?: state.name)
            }

            items(state.widgets) { widget ->
                when (widget) {
                    is DashboardWidget.ElectricityGraphWidget -> {
                        ElectricityGraph(dataPoints = widget.dataPoints)
                    }
                    is DashboardWidget.QuickActionsWidget -> {
                        QuickActions(
                            actions = widget.actions,
                            onNavigateToUtilities = onNavigateToUtilities,
                            onNavigateToProfile = onNavigateToProfile
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WelcomeCard(name: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
            Text(text = "👋", fontSize = 40.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Welcome back,",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun ElectricityGraph(dataPoints: List<MonthlyUsage>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Electricity Usage (Current Year)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth().height(200.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            if (dataPoints.isEmpty() || dataPoints.all { it.consumption == 0.0 }) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        "No data for this year.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                val primaryColor = MaterialTheme.colorScheme.primary
                
                Canvas(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    val maxConsumption = dataPoints.maxOfOrNull { it.consumption }?.takeIf { it > 0 } ?: 1.0
                    val barWidth = size.width / (12 * 2f) // Give some space
                    val spacing = barWidth

                    // Draw 12 months
                    for (month in 1..12) {
                        val usage = dataPoints.find { it.month == month }?.consumption ?: 0.0
                        val barHeight = (usage / maxConsumption) * size.height
                        
                        val x = (month - 1) * (barWidth + spacing) + spacing / 2
                        val y = size.height - barHeight.toFloat()
                        
                        drawRoundRect(
                            color = primaryColor,
                            topLeft = Offset(x, y),
                            size = Size(barWidth, barHeight.toFloat()),
                            cornerRadius = CornerRadius(barWidth / 2, barWidth / 2)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuickActions(
    actions: List<ActionItem>,
    onNavigateToUtilities: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Quick Actions",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Using a custom row-based layout to divide into 2 columns
        val chunkedActions = actions.chunked(2)
        for (chunk in chunkedActions) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                for (action in chunk) {
                    ClickableActionTile(
                        emoji = action.emoji,
                        title = action.title,
                        subtitle = action.subtitle,
                        onClick = {
                            when (action.route) {
                                "utilities" -> onNavigateToUtilities()
                                "profile" -> onNavigateToProfile()
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
                // Fill remaining empty spot if odd number
                if (chunk.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ClickableActionTile(
    emoji: String,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(text = emoji, fontSize = 28.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
