package com.hyuse.projectc.ui.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import kotlinx.coroutines.launch
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
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
    onNavigateToExpenses: () -> Unit = {},
    onNavigateToReminders: () -> Unit = {},
    onQuickAddExpense: (Double, String) -> Unit = { _, _ -> },
    onToggleInlineQuickAdd: (Boolean) -> Unit = {}
) {
    var isEditing by remember { mutableStateOf(false) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(32.dp))
                Text("MENU", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                NavigationDrawerItem(
                    label = { Text("Edit Dashboard", fontWeight = FontWeight.Bold) },
                    selected = false,
                    onClick = {
                        isEditing = true
                        coroutineScope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                NavigationDrawerItem(
                    label = { Text("Logout", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold) },
                    selected = false,
                    onClick = {
                        onLogout()
                        coroutineScope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }
    ) {
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
                            Text("☰", fontSize = 24.sp)
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
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 32.dp),
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
                            is DashboardWidget.QuickActionsWidget -> {
                                Column {
                                    QuickActionsLucid(
                                        actions = widget.actions,
                                        onNavigateToUtilities = onNavigateToUtilities,
                                        onNavigateToProfile = onNavigateToProfile,
                                        onNavigateToExpenses = onNavigateToExpenses,
                                        onNavigateToReminders = onNavigateToReminders,
                                        onToggleInlineQuickAdd = onToggleInlineQuickAdd
                                    )
                                    
                                    if (state.showInlineQuickAddExpense) {
                                        Spacer(modifier = Modifier.height(16.dp))
                                        InlineQuickAddExpense(
                                            onAdd = onQuickAddExpense,
                                            onCancel = { onToggleInlineQuickAdd(false) }
                                        )
                                    }
                                }
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

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun QuickActionsLucid(
    actions: List<ActionItem>,
    onNavigateToUtilities: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToExpenses: () -> Unit = {},
    onNavigateToReminders: () -> Unit = {},
    onToggleInlineQuickAdd: (Boolean) -> Unit = {}
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
                LucidSurface(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp),
                    alphaPrimary = 0.3f,
                    alphaSecondary = 0.1f
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .combinedClickable(
                                onClick = {
                                    when (action.route) {
                                        "utilities" -> onNavigateToUtilities()
                                        "profile" -> onNavigateToProfile()
                                        "expenses" -> onNavigateToExpenses()
                                        "reminders" -> onNavigateToReminders()
                                    }
                                },
                                onLongClick = {
                                    if (action.route == "expenses") onToggleInlineQuickAdd(true)
                                }
                            )
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
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
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

@Composable
fun InlineQuickAddExpense(onAdd: (Double, String) -> Unit, onCancel: () -> Unit) {
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Quick Entry") }

    LucidSurface(
        modifier = Modifier.fillMaxWidth(),
        alphaPrimary = 0.5f,
        alphaSecondary = 0.2f
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                "QUICK EXPENSE",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Category") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onCancel) {
                    Text("CANCEL")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        val parsedAmount = amount.toDoubleOrNull()
                        if (parsedAmount != null && parsedAmount > 0) {
                            onAdd(parsedAmount, category)
                        }
                    }
                ) {
                    Text("SAVE", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
