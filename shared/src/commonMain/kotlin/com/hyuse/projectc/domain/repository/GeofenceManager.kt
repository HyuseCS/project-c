package com.hyuse.projectc.domain.repository

import com.hyuse.projectc.domain.model.Reminder

interface GeofenceManager {
    suspend fun registerGeofences(reminders: List<Reminder>)
    suspend fun unregisterGeofence(geofenceId: String)
    suspend fun unregisterAllGeofences()
}
