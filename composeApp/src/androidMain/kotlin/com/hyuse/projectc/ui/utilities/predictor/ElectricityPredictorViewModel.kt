package com.hyuse.projectc.ui.utilities.predictor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyuse.projectc.domain.model.ElectricityAppliance
import com.hyuse.projectc.domain.usecase.GetElectricityBillHistoryUseCase
import com.hyuse.projectc.domain.usecase.ObserveProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

data class PredictorSummary(
    val totalMonthlyKwh: Double = 0.0,
    val totalEstimatedCost: Double = 0.0
)

class ElectricityPredictorViewModel(
    private val getHistoryUseCase: GetElectricityBillHistoryUseCase,
    private val observeProfileUseCase: ObserveProfileUseCase
) : ViewModel() {

    private val _appliances = MutableStateFlow<List<ElectricityAppliance>>(emptyList())
    val appliances: StateFlow<List<ElectricityAppliance>> = _appliances.asStateFlow()

    private val _ratePerKwh = MutableStateFlow(0.0)
    val ratePerKwh: StateFlow<Double> = _ratePerKwh.asStateFlow()

    private val _currencySymbol = MutableStateFlow("₱")
    val currencySymbol: StateFlow<String> = _currencySymbol.asStateFlow()

    // Derived state for the summary panel
    val summary: StateFlow<PredictorSummary> = combine(_appliances, _ratePerKwh) { apps, rate ->
        val totalKwh = apps.sumOf { it.monthlyKwh }
        PredictorSummary(
            totalMonthlyKwh = totalKwh,
            totalEstimatedCost = totalKwh * rate
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PredictorSummary()
    )

    private var hasLoadedPreferences = false

    fun loadPreferences(uid: String) {
        if (hasLoadedPreferences) return // Only fetch once per session
        viewModelScope.launch {
            try {
                val history = getHistoryUseCase(uid)
                val latest = history.maxWithOrNull(compareBy({ it.billingYear }, { it.billingMonth }))
                if (latest != null) {
                    _ratePerKwh.value = latest.ratePerKwh
                }
                hasLoadedPreferences = true
            } catch (e: Exception) {
                // Silently fail, user can manually input configs
            }
        }

        viewModelScope.launch {
            observeProfileUseCase(uid).collect { profile ->
                _currencySymbol.value = profile?.currencySymbol ?: "₱"
            }
        }
    }

    fun updateRate(rate: Double) {
        if (rate >= 0) {
            _ratePerKwh.value = rate
        }
    }

    fun addAppliance(
        name: String,
        wattage: Double,
        hoursPerDay: Double,
        daysPerMonth: Int,
        quantity: Int
    ) {
        val newAppliance = ElectricityAppliance(
            id = UUID.randomUUID().toString(),
            name = name,
            wattage = wattage,
            hoursPerDay = hoursPerDay,
            daysPerMonth = daysPerMonth,
            quantity = quantity
        )
        _appliances.update { it + newAppliance }
    }

    fun removeAppliance(id: String) {
        _appliances.update { list -> list.filterNot { it.id == id } }
    }
}
