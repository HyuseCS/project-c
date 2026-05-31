package com.hyuse.projectc.ui.reminders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyuse.projectc.domain.model.LocationData
import com.hyuse.projectc.domain.model.Reminder
import com.hyuse.projectc.domain.model.ReminderImportance
import com.hyuse.projectc.domain.repository.GeofenceManager
import com.hyuse.projectc.domain.repository.ReminderRepository
import com.hyuse.projectc.domain.usecase.ObserveProfileUseCase
import com.hyuse.projectc.domain.usecase.GetCurrentUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class RemindersViewModel(
    private val reminderRepository: ReminderRepository,
    private val geofenceManager: GeofenceManager,
    private val observeProfileUseCase: ObserveProfileUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    val reminders = reminderRepository.observeAllReminders()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val userProfile = getCurrentUserUseCase()
        .flatMapLatest { user ->
            val uid = user?.uid
            if (uid != null) observeProfileUseCase(uid)
            else flowOf(null)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private val _uiState = MutableStateFlow<RemindersUiState>(RemindersUiState.Idle)
    val uiState: StateFlow<RemindersUiState> = _uiState.asStateFlow()

    fun saveReminder(
        title: String,
        description: String,
        dateMillis: Long,
        timeMillis: Long,
        importance: ReminderImportance,
        latitude: Double,
        longitude: Double,
        radius: Double
    ) {
        viewModelScope.launch {
            _uiState.value = RemindersUiState.Saving
            try {
                // Free tier limit validation
                val todayReminders = reminders.value.filter { isSameDay(it.dateMillis, dateMillis) }
                if (todayReminders.size >= 3) {
                    _uiState.value = RemindersUiState.Error("Daily limit of 3 reminders reached.")
                    return@launch
                }

                // Create Geofence ID based on lat/lng (simple grouping)
                val geofenceId = "geo_${latitude}_${longitude}"

                val reminder = Reminder(
                    id = UUID.randomUUID().toString(), // Simple UUID placeholder
                    title = title,
                    description = description,
                    dateMillis = dateMillis,
                    timeMillis = timeMillis,
                    importance = importance,
                    location = LocationData(latitude, longitude, radius),
                    geofenceId = geofenceId
                )

                reminderRepository.saveReminder(reminder)
                
                // Re-register geofences
                val currentReminders = reminderRepository.getAllReminders()
                geofenceManager.registerGeofences(currentReminders)

                _uiState.value = RemindersUiState.Success
            } catch (e: Exception) {
                _uiState.value = RemindersUiState.Error(e.message ?: "Failed to save reminder")
            }
        }
    }

    fun deleteReminder(id: String) {
        viewModelScope.launch {
            try {
                reminderRepository.deleteReminder(id)
                val currentReminders = reminderRepository.getAllReminders()
                
                if (currentReminders.isEmpty()) {
                    geofenceManager.unregisterAllGeofences()
                } else {
                    geofenceManager.registerGeofences(currentReminders)
                }
            } catch (e: Exception) {
                _uiState.value = RemindersUiState.Error("Failed to delete reminder")
            }
        }
    }

    fun resetState() {
        _uiState.value = RemindersUiState.Idle
    }

    private fun isSameDay(time1: Long, time2: Long): Boolean {
        // Simplified day check; in a real app use Calendar/LocalDate
        val dayMillis = 24 * 60 * 60 * 1000L
        return (time1 / dayMillis) == (time2 / dayMillis)
    }
}

sealed class RemindersUiState {
    object Idle : RemindersUiState()
    object Saving : RemindersUiState()
    object Success : RemindersUiState()
    data class Error(val message: String) : RemindersUiState()
}
