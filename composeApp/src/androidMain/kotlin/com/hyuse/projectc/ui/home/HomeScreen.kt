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

import androidx.compose.runtime.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeState,
    onLogout: () -> Unit,
    onUpdateWidgets: (List<String>) -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToUtilities: () -> Unit = {},
    onNavigateToExpenses: () -> Unit = {}
) {
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Project C", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { showSheet = true }) {
                        Text("⚙️", fontSize = 24.sp)
                    }
                    TextButton(onClick = onLogout) { Text("Logout") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (state.isProfileMissing) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Profile not found.", style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onNavigateToProfile) {
                        Text("Complete Profile")
                    }
                }
            } else if (state.error != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: ${state.error}", color = MaterialTheme.colorScheme.error)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    contentPadding = PaddingValues(vertical = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    items(state.widgets) { widget ->
                        when (widget) {
                            is DashboardWidget.WelcomeWidget -> {
                                WelcomeCard(name = widget.nickname ?: widget.name)
                            }
                            is DashboardWidget.ElectricityGraphWidget -> {
                                UsageGraph(
                                    title = "Electricity Consumption (kWh)",
                                    dataPoints = widget.dataPoints,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            is DashboardWidget.WaterUsageWidget -> {
                                UsageGraph(
                                    title = "Water Consumption (Units)",
                                    dataPoints = widget.dataPoints,
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                            }
                            is DashboardWidget.ExpenseSummaryWidget -> {
                                ExpenseSummaryCard(summary = widget.summary)
                            }
                            is DashboardWidget.RecentActivityFeedWidget -> {
                                RecentActivityFeed(activities = widget.activities)
                            }
                            is DashboardWidget.QuickActionsWidget -> {
                                QuickActions(
                                    actions = widget.actions,
                                    onNavigateToUtilities = onNavigateToUtilities,
                                    onNavigateToProfile = onNavigateToProfile,
                                    onNavigateToExpenses = onNavigateToExpenses
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState
        ) {
            DashboardEditorContent(
                availableWidgets = state.availableWidgets,
                activeWidgets = state.activeWidgets,
                onUpdate = onUpdateWidgets
            )
        }
    }
}

@Composable
fun DashboardEditorContent(
    availableWidgets: List<String>,
    activeWidgets: List<String>,
    onUpdate: (List<String>) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .padding(bottom = 32.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Edit Dashboard",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = { onUpdate(availableWidgets) }) {
                Text("Reset All")
            }
        }
        
        Text(
            text = "Toggle the modules you want to see on your home screen.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        availableWidgets.forEach { widgetKey ->
            val isActive = activeWidgets.contains(widgetKey)
            val title = when (widgetKey) {
                "welcome" -> "Welcome Greeting"
                "electricity" -> "Electricity Graph"
                "water" -> "Water Usage Trend"
                "expenses" -> "Expense Summary"
                "activity" -> "Recent Activity Feed"
                "actions" -> "Quick Actions"
                else -> widgetKey.replaceFirstChar { it.uppercase() }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Switch(
                    checked = isActive,
                    onCheckedChange = { checked ->
                        val newList = if (checked) {
                            activeWidgets + widgetKey
                        } else {
                            activeWidgets - widgetKey
                        }
                        // Maintain order as in availableWidgets
                        onUpdate(availableWidgets.filter { newList.contains(it) })
                    }
                )
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
fun UsageGraph(title: String, dataPoints: List<MonthlyUsage>, color: androidx.compose.ui.graphics.Color) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
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
                Canvas(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    val maxConsumption = dataPoints.maxOfOrNull { it.consumption }?.takeIf { it > 0 } ?: 1.0
                    val barWidth = size.width / (12 * 2f)
                    val spacing = barWidth

                    for (month in 1..12) {
                        val usage = dataPoints.find { it.month == month }?.consumption ?: 0.0
                        val barHeight = (usage / maxConsumption) * size.height
                        
                        val x = (month - 1) * (barWidth + spacing) + spacing / 2
                        val y = size.height - barHeight.toFloat()
                        
                        drawRoundRect(
                            color = color,
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
fun ExpenseSummaryCard(summary: ExpenseSummary) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Monthly Spending",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "Total Spent",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                )
                Text(
                    text = "${summary.currencySymbol} ${"%.2f".format(summary.total)}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                
                if (summary.categoryBreakdown.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.1f))
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    summary.categoryBreakdown.toList().sortedByDescending { it.second }.take(3).forEach { (category, amount) ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(category, style = MaterialTheme.typography.bodyMedium)
                            Text("${summary.currencySymbol} ${"%.2f".format(amount)}", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RecentActivityFeed(activities: List<RecentActivity>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Recent Activity",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                if (activities.isEmpty()) {
                    Text("No recent activity.", modifier = Modifier.padding(16.dp))
                } else {
                    activities.forEachIndexed { index, activity ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val icon = when (activity.type) {
                                "utility" -> "⚡"
                                "expense" -> "💰"
                                else -> "📋"
                            }
                            Text(icon, fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(activity.title, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyLarge)
                                Text(
                                    text = activity.subtitle,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Text(
                                "${activity.currencySymbol} ${activity.amount}",
                                fontWeight = FontWeight.Bold,
                                color = if (activity.type == "expense") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                            )
                        }
                        if (index < activities.size - 1) {
                            HorizontalDivider(
                                modifier = Modifier.padding(start = 40.dp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f)
                            )
                        }
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
    onNavigateToProfile: () -> Unit,
    onNavigateToExpenses: () -> Unit = {}
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
                                "expenses" -> onNavigateToExpenses()
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
