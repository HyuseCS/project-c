package com.hyuse.projectc.domain.model

data class Reminder(
    val id: String,
    val title: String,
    val description: String,
    val dateMillis: Long,
    val timeMillis: Long,
    val importance: ReminderImportance,
    val location: LocationData,
    val lastTriggeredMillis: Long? = null,
    val geofenceId: String
)
