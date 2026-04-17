package com.hyuse.projectc.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyuse.projectc.domain.model.ElectricityBillResult
import com.hyuse.projectc.domain.model.Expense
import com.hyuse.projectc.domain.model.UserProfile
import com.hyuse.projectc.domain.model.WaterBillResult
import com.hyuse.projectc.domain.usecase.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar

import java.text.DateFormatSymbols

data class MonthlyUsage(val month: Int, val consumption: Double, val cost: Double)

data class ExpenseSummary(val total: Double, val categoryBreakdown: Map<String, Double>, val currencySymbol: String)

data class RecentActivity(val title: String, val subtitle: String, val date: Long, val amount: String, val type: String, val currencySymbol: String)

data class ActionItem(val title: String, val subtitle: String, val emoji: String, val route: String)

sealed class DashboardWidget {
    data class WelcomeWidget(val nickname: String?, val name: String) : DashboardWidget()
    data class ElectricityGraphWidget(val dataPoints: List<MonthlyUsage>) : DashboardWidget()
    data class WaterUsageWidget(val dataPoints: List<MonthlyUsage>) : DashboardWidget()
    data class ExpenseSummaryWidget(val summary: ExpenseSummary) : DashboardWidget()
    data class RecentActivityFeedWidget(val activities: List<RecentActivity>) : DashboardWidget()
    data class QuickActionsWidget(val actions: List<ActionItem>) : DashboardWidget()
}

/**
 * UI State for the Home screen.
 */
data class HomeState(
    val isLoading: Boolean = true,
    val isProfileMissing: Boolean = false,
    val widgets: List<DashboardWidget> = emptyList(),
    val availableWidgets: List<String> = listOf("welcome", "electricity", "water", "expenses", "activity", "actions"),
    val activeWidgets: List<String> = emptyList(),
    val error: String? = null
)

/**
 * ViewModel for the Home screen.
 * Observes the user profile and builds dashboard widgets.
 */
class HomeViewModel(
    private val observeProfileUseCase: ObserveProfileUseCase,
    private val observeElectricityBillHistoryUseCase: ObserveElectricityBillHistoryUseCase,
    private val observeWaterBillHistoryUseCase: ObserveWaterBillHistoryUseCase,
    private val observeMonthlyExpensesUseCase: ObserveMonthlyExpensesUseCase,
    private val saveProfileUseCase: SaveProfileUseCase
) : ViewModel() {

    private val _homeState = MutableStateFlow(HomeState())
    val homeState: StateFlow<HomeState> = _homeState.asStateFlow()

    private var currentProfile: UserProfile? = null

    /**
     * Loads the dashboard for the given [uid].
     */
    fun loadDashboard(uid: String) {
        _homeState.update { it.copy(isLoading = true) }
        
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val startTime = calendar.timeInMillis
        
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        val endTime = calendar.timeInMillis

        viewModelScope.launch {
            combine(
                observeProfileUseCase(uid),
                observeElectricityBillHistoryUseCase(uid),
                observeWaterBillHistoryUseCase(uid),
                observeMonthlyExpensesUseCase(uid, startTime, endTime)
            ) { profile, electricityHistory, waterHistory, expenses ->
                currentProfile = profile
                if (profile == null) {
                    HomeState(isLoading = false, isProfileMissing = true)
                } else {
                    val widgets = buildDashboardWidgets(profile, electricityHistory, waterHistory, expenses)
                    HomeState(
                        isLoading = false, 
                        widgets = widgets,
                        activeWidgets = profile.dashboardWidgets
                    )
                }
            }.catch { e ->
                _homeState.update { it.copy(isLoading = false, error = e.message ?: "Unknown error") }
            }.collect { state ->
                _homeState.value = state
            }
        }
    }

    /**
     * Updates the user's dashboard widget preferences.
     */
    fun updateWidgets(widgets: List<String>) {
        val profile = currentProfile ?: return
        viewModelScope.launch {
            saveProfileUseCase(profile.copy(dashboardWidgets = widgets))
        }
    }

    private fun buildDashboardWidgets(
        profile: UserProfile,
        electricityHistory: List<ElectricityBillResult>,
        waterHistory: List<WaterBillResult>,
        expenses: List<Expense>
    ): List<DashboardWidget> {
        val widgets = mutableListOf<DashboardWidget>()
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val preferredWidgetKeys = profile.dashboardWidgets

        // 1. Welcome Widget
        if (preferredWidgetKeys.contains("welcome")) {
            widgets.add(DashboardWidget.WelcomeWidget(nickname = profile.nickname, name = profile.name))
        }

        // 2. Electricity Graph
        if (preferredWidgetKeys.contains("electricity")) {
            val currentYearElectricity = electricityHistory.filter { it.billingYear == currentYear }
            val electricityDataPoints = (1..12).map { month ->
                val monthBills = currentYearElectricity.filter { it.billingMonth == month }
                MonthlyUsage(
                    month = month,
                    consumption = monthBills.sumOf { it.consumption },
                    cost = monthBills.sumOf { it.totalCost }
                )
            }
            widgets.add(DashboardWidget.ElectricityGraphWidget(dataPoints = electricityDataPoints))
        }

        // 3. Water Usage Graph
        if (preferredWidgetKeys.contains("water")) {
            val currentYearWater = waterHistory.filter { it.billingYear == currentYear }
            val waterDataPoints = (1..12).map { month ->
                val monthBills = currentYearWater.filter { it.billingYear == currentYear && it.billingMonth == month }
                MonthlyUsage(
                    month = month,
                    consumption = monthBills.sumOf { it.consumption },
                    cost = monthBills.sumOf { it.totalCost }
                )
            }
            widgets.add(DashboardWidget.WaterUsageWidget(dataPoints = waterDataPoints))
        }

        // 4. Expense Summary
        if (preferredWidgetKeys.contains("expenses")) {
            val totalExpense = expenses.sumOf { it.amount }
            val breakdown = expenses.groupBy { it.categoryName }
                .mapValues { it.value.sumOf { e -> e.amount } }
            widgets.add(DashboardWidget.ExpenseSummaryWidget(ExpenseSummary(totalExpense, breakdown, profile.currencySymbol)))
        }

        // 5. Recent Activity Feed
        if (preferredWidgetKeys.contains("activity")) {
            val activities = mutableListOf<RecentActivity>()
            val months = DateFormatSymbols().months
            
            electricityHistory.take(2).forEach {
                val monthName = if (it.billingMonth in 1..12) months[it.billingMonth - 1] else ""
                val subtitle = "$monthName ${it.billingYear} Bill"
                activities.add(RecentActivity("Electricity", subtitle, it.timestamp, "%.2f".format(it.totalCost), "utility", profile.currencySymbol))
            }
            waterHistory.take(2).forEach {
                val monthName = if (it.billingMonth in 1..12) months[it.billingMonth - 1] else ""
                val subtitle = "$monthName ${it.billingYear} Bill"
                activities.add(RecentActivity("Water", subtitle, it.timestamp, "%.2f".format(it.totalCost), "utility", profile.currencySymbol))
            }
            expenses.take(2).forEach {
                activities.add(RecentActivity(it.description, it.categoryName, it.timestamp, "%.2f".format(it.amount), "expense", profile.currencySymbol))
            }
            widgets.add(DashboardWidget.RecentActivityFeedWidget(activities.sortedByDescending { it.date }.take(5)))
        }

        // 6. Quick Actions
        if (preferredWidgetKeys.contains("actions")) {
            widgets.add(
                DashboardWidget.QuickActionsWidget(
                    actions = listOf(
                        ActionItem("Utilities", "Calculators & tools", "🛠️", "utilities"),
                        ActionItem("Expenses", "Track expenses", "💰", "expenses"),
                        ActionItem("Profile", "View and edit", "👤", "profile")
                    )
                )
            )
        }

        return widgets
    }
}
