package com.hyuse.projectc.ui.utilities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyuse.projectc.domain.model.WaterBillResult
import com.hyuse.projectc.domain.usecase.CalculateWaterBillUseCase
import com.hyuse.projectc.domain.usecase.GetWaterBillHistoryUseCase
import com.hyuse.projectc.domain.usecase.SaveWaterBillUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Represents the UI state for the water bill calculator.
 */
sealed class WaterBillUiState {
    data object Idle : WaterBillUiState()
    data object Saving : WaterBillUiState()
    data class Calculated(val result: WaterBillResult) : WaterBillUiState()
    data class ConfirmOverwrite(val existingResultId: String, val newResult: WaterBillResult) : WaterBillUiState()
    data object SaveSuccess : WaterBillUiState()
    data class Error(val message: String) : WaterBillUiState()
}

/**
 * ViewModel for the Water Bill Calculator screen.
 */
class WaterBillViewModel(
    private val calculateUseCase: CalculateWaterBillUseCase,
    private val saveUseCase: SaveWaterBillUseCase,
    private val getHistoryUseCase: GetWaterBillHistoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<WaterBillUiState>(WaterBillUiState.Idle)
    val uiState: StateFlow<WaterBillUiState> = _uiState.asStateFlow()

    private val _history = MutableStateFlow<List<WaterBillResult>>(emptyList())
    val history: StateFlow<List<WaterBillResult>> = _history.asStateFlow()

    private var lastResult: WaterBillResult? = null

    val currencies = listOf(
        "₱ (Philippines)" to "₱",
        "$ (USA)" to "$",
        "€ (Eurozone)" to "€",
        "£ (UK)" to "£",
        "¥ (Japan)" to "¥",
        "₩ (South Korea)" to "₩"
    )

    fun loadHistory(uid: String) {
        viewModelScope.launch {
            try {
                _history.value = getHistoryUseCase(uid)
            } catch (e: Exception) {
                // Silently fail
            }
        }
    }

    fun getLastRate(): Double? = _history.value.firstOrNull()?.ratePerCubicMeter
    fun getLastCurrency(): String = _history.value.firstOrNull()?.currencySymbol ?: "₱"

    fun getPreviousReadingFor(month: Int, year: Int): String {
        val prevBill = _history.value
            .filter { it.billingYear < year || (it.billingYear == year && it.billingMonth < month) }
            .maxWithOrNull(compareBy({ it.billingYear }, { it.billingMonth }))
            
        return prevBill?.currentReading?.toString() ?: ""
    }

    fun calculate(
        billingMonth: Int,
        billingYear: Int,
        previousReading: String,
        currentReading: String,
        ratePerCubicMeter: String,
        currencySymbol: String
    ) {
        val prev = previousReading.toDoubleOrNull()
        val curr = currentReading.toDoubleOrNull()
        val rate = ratePerCubicMeter.toDoubleOrNull()

        if (prev == null || curr == null || rate == null) {
            _uiState.value = WaterBillUiState.Error("Please enter valid numbers for all fields.")
            return
        }

        if (curr < prev) {
            _uiState.value = WaterBillUiState.Error("Current reading must be greater than or equal to previous reading.")
            return
        }

        if (rate <= 0) {
            _uiState.value = WaterBillUiState.Error("Rate must be greater than zero.")
            return
        }

        val result = calculateUseCase(
            billingMonth = billingMonth,
            billingYear = billingYear,
            previousReading = prev,
            currentReading = curr,
            ratePerCubicMeter = rate,
            currencySymbol = currencySymbol.ifBlank { "₱" },
            timestamp = System.currentTimeMillis()
        )
        lastResult = result
        _uiState.value = WaterBillUiState.Calculated(result)
    }

    fun saveResult(uid: String, forceOverwrite: Boolean = false, overwriteId: String? = null) {
        val result = lastResult ?: return
        
        if (!forceOverwrite) {
            val existing = _history.value.find { it.billingMonth == result.billingMonth && it.billingYear == result.billingYear }
            if (existing != null) {
                _uiState.value = WaterBillUiState.ConfirmOverwrite(existing.id, result)
                return
            }
        }

        _uiState.value = WaterBillUiState.Saving
        viewModelScope.launch {
            try {
                val finalResult = if (overwriteId != null) result.copy(id = overwriteId) else result
                saveUseCase(uid, finalResult)
                lastResult = null
                _uiState.value = WaterBillUiState.SaveSuccess
                _history.value = getHistoryUseCase(uid)
            } catch (e: Exception) {
                _uiState.value = WaterBillUiState.Error(e.message ?: "Failed to save calculation.")
            }
        }
    }

    fun resetState() {
        _uiState.value = WaterBillUiState.Idle
    }
}
