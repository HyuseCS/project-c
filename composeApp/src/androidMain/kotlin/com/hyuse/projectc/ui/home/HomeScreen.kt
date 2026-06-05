package com.hyuse.projectc.ui.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import kotlinx.coroutines.launch
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hyuse.projectc.ui.theme.LucidSurface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Edit
import com.hyuse.projectc.ui.components.LocalDrawerState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeState,
    onLogout: () -> Unit,
    onUpdateWidgets: (List<String>) -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToUtilities: () -> Unit = {},
    onNavigateToExpenses: () -> Unit = {},
    onNavigateToReminders: () -> Unit = {},
    onQuickAddExpense: (Double, String) -> Unit = { _, _ -> },
    onToggleInlineQuickAdd: (Boolean) -> Unit = {}
) {
    var isEditing by remember { mutableStateOf(false) }
    val drawerState = LocalDrawerState.current
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        if (isEditing) {
            ModalBottomSheet(
                onDismissRequest = { isEditing = false },
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                DashboardEditorContent(
                    availableWidgets = state.availableWidgets,
                    activeWidgets = state.activeWidgets,
                    onUpdate = onUpdateWidgets
                )
            }
        }

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
                    navigationIcon = {
                        IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        IconButton(onClick = { isEditing = true }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit Dashboard")
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
                    var visible by remember { mutableStateOf(false) }
                    LaunchedEffect(Unit) {
                        visible = true
                    }
                    
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 32.dp, bottom = 120.dp),
                        verticalArrangement = Arrangement.spacedBy(48.dp)
                    ) {
                        items(state.widgets.size, key = { state.widgets[it].javaClass.name }) { index ->
                            val widget = state.widgets[index]
                            androidx.compose.animation.AnimatedVisibility(
                                visible = visible,
                                enter = androidx.compose.animation.slideInVertically(
                                    initialOffsetY = { 50 },
                                    animationSpec = androidx.compose.animation.core.tween(
                                        durationMillis = 600,
                                        delayMillis = index * 100,
                                        easing = androidx.compose.animation.core.FastOutSlowInEasing
                                    )
                                ) + androidx.compose.animation.fadeIn(
                                    animationSpec = androidx.compose.animation.core.tween(
                                        durationMillis = 600,
                                        delayMillis = index * 100
                                    )
                                )
                            ) {
                                when (widget) {
                                    is DashboardWidget.WelcomeWidget -> {
                                        WelcomeHero(widget = widget)
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
                                    else -> {} // QuickActionsWidget is now handled globally in the sidebar
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
fun WelcomeHero(widget: DashboardWidget.WelcomeWidget) {
    LucidSurface(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(28.dp)) {
            Text(
                text = widget.greeting,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 36.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = widget.actionableSummary ?: "Everything is balanced and under control.",
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
            Canvas(modifier = Modifier.fillMaxSize()) {
                val maxConsumption = dataPoints.maxOfOrNull { it.consumption }?.takeIf { it > 0 } ?: 1.0
                val barWidth = size.width / (12 * 2.5f)
                val spacing = barWidth

                for (month in 1..12) {
                    val usage = dataPoints.find { it.month == month }?.consumption ?: 0.0
                    val x = (month - 1) * (barWidth + spacing) + spacing / 2
                    
                    if (usage <= 0.0) {
                        // Skeletal placeholder
                        drawRoundRect(
                            color = color.copy(alpha = 0.05f),
                            topLeft = Offset(x, 0f),
                            size = Size(barWidth, size.height),
                            cornerRadius = CornerRadius(barWidth / 2, barWidth / 2)
                        )
                    } else {
                        val barHeight = (usage / maxConsumption) * size.height
                        val y = size.height - barHeight.toFloat()
                        
                        drawRoundRect(
                            brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                                colors = listOf(color, color.copy(alpha = 0.3f)),
                                startY = y,
                                endY = size.height
                            ),
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
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface
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
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "${summary.currencySymbol}${ "%.2f".format(amount) }",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}

@Composable
fun ActivityFeed(activities: List<RecentActivity>) {
    LucidSurface(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
            Text(
                text = "RECENT ACTIVITY",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (activities.isEmpty()) {
                Text(
                    "No history yet.", 
                    style = MaterialTheme.typography.bodyLarge, 
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
            } else {
                activities.forEach { activity ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                activity.title, 
                                fontWeight = FontWeight.Normal, 
                                style = MaterialTheme.typography.bodyLarge, 
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = activity.subtitle.uppercase(),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            )
                        }
                        Text(
                            "${activity.currencySymbol}${activity.amount}",
                            fontWeight = FontWeight.ExtraBold,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
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
            .padding(horizontal = 32.dp, vertical = 24.dp)
    ) {
        Text(
            text = "EDIT DASHBOARD",
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
        
        Spacer(modifier = Modifier.height(48.dp))
    }
}

