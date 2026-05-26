package com.hyuse.projectc.ui.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hyuse.projectc.ui.theme.LucidSurface

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
                title = { 
                    Text(
                        "PROJECT C", 
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.titleLarge,
                        letterSpacing = 2.sp
                    ) 
                },
                actions = {
                    IconButton(onClick = { showSheet = true }) {
                        Text("⋮", fontWeight = FontWeight.Bold, fontSize = 24.sp)
                    }
                    TextButton(onClick = onLogout) { 
                        Text("LOGOUT", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary) 
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            if (state.isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter))
            } else if (state.isProfileMissing) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(32.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        "Ready to\nElevate?", 
                        style = MaterialTheme.typography.headlineLarge, 
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 40.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Initialize your profile to begin.", 
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = onNavigateToProfile,
                        modifier = Modifier.height(56.dp).fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text("GET STARTED", fontWeight = FontWeight.Bold)
                    }
                }
            } else if (state.error != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Connection issue. Retrying...", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(48.dp)
                ) {
                    items(state.widgets) { widget ->
                        when (widget) {
                            is DashboardWidget.WelcomeWidget -> {
                                WelcomeHero(name = widget.nickname ?: widget.name)
                            }
                            is DashboardWidget.QuickActionsWidget -> {
                                QuickActionsLucid(
                                    actions = widget.actions,
                                    onNavigateToUtilities = onNavigateToUtilities,
                                    onNavigateToProfile = onNavigateToProfile,
                                    onNavigateToExpenses = onNavigateToExpenses
                                )
                            }
                            is DashboardWidget.ElectricityGraphWidget -> {
                                UsageInsights(
                                    title = "Electricity",
                                    dataPoints = widget.dataPoints,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            is DashboardWidget.WaterUsageWidget -> {
                                UsageInsights(
                                    title = "Water Usage",
                                    dataPoints = widget.dataPoints,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                            is DashboardWidget.ExpenseSummaryWidget -> {
                                SpendingOverview(summary = widget.summary)
                            }
                            is DashboardWidget.RecentActivityFeedWidget -> {
                                ActivityFeed(activities = widget.activities)
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
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RectangleShape
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
fun WelcomeHero(name: String) {
    LucidSurface(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(28.dp)) {
            Text(
                text = "HELLO,",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = name.uppercase(),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Everything is balanced and under control.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun UsageInsights(title: String, dataPoints: List<MonthlyUsage>, color: androidx.compose.ui.graphics.Color) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            if (dataPoints.isEmpty() || dataPoints.all { it.consumption == 0.0 }) {
                Text(
                    "Collecting data...", 
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
            } else {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val maxConsumption = dataPoints.maxOfOrNull { it.consumption }?.takeIf { it > 0 } ?: 1.0
                    val barWidth = size.width / (12 * 2.5f)
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
                            cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SpendingOverview(summary: ExpenseSummary) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "MONTHLY SPENDING",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "${summary.currencySymbol}${ "%.2f".format(summary.total) }",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold
        )
        
        if (summary.categoryBreakdown.isNotEmpty()) {
            Spacer(modifier = Modifier.height(24.dp))
            summary.categoryBreakdown.toList().sortedByDescending { it.second }.take(3).forEach { (category, amount) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        category.uppercase(), 
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "${summary.currencySymbol}${ "%.2f".format(amount) }",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
            }
        }
    }
}

@Composable
fun ActivityFeed(activities: List<RecentActivity>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "RECENT ACTIVITY",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (activities.isEmpty()) {
            Text("No history yet.", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f))
        } else {
            activities.forEach { activity ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(activity.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                        Text(
                            text = activity.subtitle.uppercase(),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                    Text(
                        "${activity.currencySymbol}${activity.amount}",
                        fontWeight = FontWeight.Black,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
            }
        }
    }
}

@Composable
fun QuickActionsLucid(
    actions: List<ActionItem>,
    onNavigateToUtilities: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToExpenses: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "QUICK ACTIONS",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            actions.forEach { action ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                        .clickable {
                            when (action.route) {
                                "utilities" -> onNavigateToUtilities()
                                "profile" -> onNavigateToProfile()
                                "expenses" -> onNavigateToExpenses()
                            }
                        }
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(action.emoji, fontSize = 28.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            action.title.uppercase(),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
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
            .padding(32.dp)
    ) {
        Text(
            text = "HUB_CONFIG",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold
        )
        
        Spacer(modifier = Modifier.height(32.dp))

        availableWidgets.forEach { widgetKey ->
            val isActive = activeWidgets.contains(widgetKey)
            val title = when (widgetKey) {
                "welcome" -> "Welcome Hero"
                "electricity" -> "Electricity Insights"
                "water" -> "Water Stats"
                "expenses" -> "Spending Summary"
                "activity" -> "Recent History"
                "actions" -> "Quick Access"
                else -> widgetKey.replaceFirstChar { it.uppercase() }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    title.uppercase(), 
                    style = MaterialTheme.typography.bodyLarge, 
                    fontWeight = FontWeight.Bold
                )
                Switch(
                    checked = isActive,
                    onCheckedChange = { checked ->
                        val newList = if (checked) {
                            activeWidgets + widgetKey
                        } else {
                            activeWidgets - widgetKey
                        }
                        onUpdate(availableWidgets.filter { newList.contains(it) })
                    }
                )
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
        }
    }
}
