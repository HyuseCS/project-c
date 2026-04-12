package com.hyuse.projectc.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyuse.projectc.domain.model.UserProfile
import com.hyuse.projectc.domain.usecase.GetElectricityBillHistoryUseCase
import com.hyuse.projectc.domain.usecase.ObserveProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar

data class MonthlyUsage(val month: Int, val consumption: Double, val cost: Double)

data class ActionItem(val title: String, val subtitle: String, val emoji: String, val route: String)

sealed class DashboardWidget {
    data class ElectricityGraphWidget(val dataPoints: List<MonthlyUsage>) : DashboardWidget()
    data class QuickActionsWidget(val actions: List<ActionItem>) : DashboardWidget()
}

/**
 * UI State for the Home screen.
 */
sealed class HomeState {
    /** Initial state */
    data object Loading : HomeState()

    /** User profile does not exist in Firestore */
    data object ProfileMissing : HomeState()

    /** User profile exists */
    data class Success(
        val nickname: String?,
        val name: String,
        val widgets: List<DashboardWidget>
    ) : HomeState()

    /** An error occurred */
    data class Error(val message: String) : HomeState()
}

/**
 * ViewModel for the Home screen.
 * Observes the user profile and builds dashboard widgets.
 */
class HomeViewModel(
    private val observeProfileUseCase: ObserveProfileUseCase,
    private val getElectricityBillHistoryUseCase: GetElectricityBillHistoryUseCase
) : ViewModel() {

    private val _homeState = MutableStateFlow<HomeState>(HomeState.Loading)
    val homeState: StateFlow<HomeState> = _homeState.asStateFlow()

    /**
     * Loads the dashboard for the given [uid].
     */
    fun loadDashboard(uid: String) {
        _homeState.value = HomeState.Loading
        viewModelScope.launch {
            observeProfileUseCase(uid)
                .catch { e ->
                    _homeState.value = HomeState.Error(e.message ?: "Failed to observe profile")
                }
                .collectLatest { profile ->
                    if (profile == null) {
                        _homeState.value = HomeState.ProfileMissing
                    } else {
                        buildDashboardWidgets(uid, profile)
                    }
                }
        }
    }

    private suspend fun buildDashboardWidgets(uid: String, profile: UserProfile) {
        try {
            val history = getElectricityBillHistoryUseCase(uid)
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            
            // Filter and aggregate monthly usage for the current year
            val currentYearBills = history.filter { it.billingYear == currentYear }
            val monthlyUsages = (1..12).map { month ->
                val monthBills = currentYearBills.filter { it.billingMonth == month }
                val totalConsumption = monthBills.sumOf { it.consumption }
                val totalCost = monthBills.sumOf { it.totalCost }
                MonthlyUsage(month = month, consumption = totalConsumption, cost = totalCost)
            }

            val electricityGraphWidget = DashboardWidget.ElectricityGraphWidget(dataPoints = monthlyUsages)

            val quickActionsWidget = DashboardWidget.QuickActionsWidget(
                actions = listOf(
                    ActionItem("Utilities", "Calculators & tools", "🛠️", "utilities"),
                    ActionItem("Profile", "View and edit", "👤", "profile")
                )
            )

            _homeState.value = HomeState.Success(
                nickname = profile.nickname,
                name = profile.name,
                widgets = listOf(electricityGraphWidget, quickActionsWidget)
            )
        } catch (e: Exception) {
            _homeState.value = HomeState.Error(e.message ?: "Failed to build dashboard")
        }
    }
}
