package com.hyuse.projectc.ui.utilities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyuse.projectc.domain.model.ElectricityBillResult
import com.hyuse.projectc.domain.usecase.CalculateElectricityBillUseCase
import com.hyuse.projectc.domain.usecase.GetElectricityBillHistoryUseCase
import com.hyuse.projectc.domain.usecase.ObserveProfileUseCase
import com.hyuse.projectc.domain.usecase.SaveElectricityBillUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

/**
 * Represents the UI state for the electricity bill calculator.
 */
sealed class ElectricBillUiState {
    data object Idle : ElectricBillUiState()
    data object Saving : ElectricBillUiState()
    data class Calculated(val result: ElectricityBillResult) : ElectricBillUiState()
    data class ConfirmOverwrite(val existingResultId: String, val newResult: ElectricityBillResult) : ElectricBillUiState()
    data object SaveSuccess : ElectricBillUiState()
    data class Error(val message: String) : ElectricBillUiState()
}

/**
 * ViewModel for the Electricity Bill Calculator screen.
 * Handles calculation, saving, and history retrieval.
 */
class ElectricityBillViewModel(
    private val calculateUseCase: CalculateElectricityBillUseCase,
    private val saveUseCase: SaveElectricityBillUseCase,
    private val getHistoryUseCase: GetElectricityBillHistoryUseCase,
    private val observeProfileUseCase: ObserveProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ElectricBillUiState>(ElectricBillUiState.Idle)
    val uiState: StateFlow<ElectricBillUiState> = _uiState.asStateFlow()

    private val _history = MutableStateFlow<List<ElectricityBillResult>>(emptyList())
    val history: StateFlow<List<ElectricityBillResult>> = _history.asStateFlow()

    private val _currencySymbol = MutableStateFlow("₱")
    val currencySymbol: StateFlow<String> = _currencySymbol.asStateFlow()

    /** The last calculated result, held for saving. */
    private var lastResult: ElectricityBillResult? = null

    fun loadHistory(uid: String) {
        viewModelScope.launch {
            try {
                _history.value = getHistoryUseCase(uid)
            } catch (e: Exception) {
                // Silently fail — history is non-critical
            }
        }

        viewModelScope.launch {
            observeProfileUseCase(uid).collect { profile ->
                _currencySymbol.value = profile?.currencySymbol ?: "₱"
            }
        }
    }

    fun getLastRate(): Double? = _history.value.firstOrNull()?.ratePerKwh

    /**
     * Determines what the previous reading should be, based on selecting a billing month and year.
     * Looks for the most recent chronological bill occurring *before* the selected month/year.
     */
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
        ratePerKwh: String
    ) {
        val prev = previousReading.toDoubleOrNull()
        val curr = currentReading.toDoubleOrNull()
        val rate = ratePerKwh.toDoubleOrNull()

        if (prev == null || curr == null || rate == null) {
            _uiState.value = ElectricBillUiState.Error("Please enter valid numbers for all fields.")
            return
        }

        if (curr < prev) {
            _uiState.value = ElectricBillUiState.Error("Current reading must be greater than or equal to previous reading.")
            return
        }

        if (rate <= 0) {
            _uiState.value = ElectricBillUiState.Error("Rate must be greater than zero.")
            return
        }

        val result = calculateUseCase(
            billingMonth = billingMonth,
            billingYear = billingYear,
            previousReading = prev,
            currentReading = curr,
            ratePerKwh = rate,
            currencySymbol = _currencySymbol.value,
            timestamp = System.currentTimeMillis()
        )
        lastResult = result
        _uiState.value = ElectricBillUiState.Calculated(result)
    }

    /**
     * Saves the last calculated result.
     * If [forceOverwrite] is false, checks for duplicate month/year first.
     */
    fun saveResult(uid: String, forceOverwrite: Boolean = false, overwriteId: String? = null) {
        val result = lastResult ?: return
        
        if (!forceOverwrite) {
            val existing = _history.value.find { it.billingMonth == result.billingMonth && it.billingYear == result.billingYear }
            if (existing != null) {
                // Show confirmation state to user
                _uiState.value = ElectricBillUiState.ConfirmOverwrite(existing.id, result)
                return
            }
        }

        _uiState.value = ElectricBillUiState.Saving
        viewModelScope.launch {
            try {
                // Attach the overwritten ID if we are replacing an old calculation
                val finalResult = if (overwriteId != null) result.copy(id = overwriteId) else result
                
                saveUseCase(uid, finalResult)
                lastResult = null
                _uiState.value = ElectricBillUiState.SaveSuccess
                _history.value = getHistoryUseCase(uid)
            } catch (e: Exception) {
                _uiState.value = ElectricBillUiState.Error(e.message ?: "Failed to save calculation.")
            }
        }
    }

    fun resetState() {
        _uiState.value = ElectricBillUiState.Idle
    }
}
