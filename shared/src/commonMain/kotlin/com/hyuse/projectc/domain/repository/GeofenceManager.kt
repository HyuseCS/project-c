package com.hyuse.projectc.domain.repository

import com.hyuse.projectc.domain.model.Reminder

enum class GeofenceProximity {
    INSIDE,
    OUTSIDE,
    UNAVAILABLE
}

interface GeofenceManager {
    suspend fun registerGeofences(reminders: List<Reminder>)
    suspend fun unregisterGeofence(geofenceId: String)
    suspend fun unregisterAllGeofences()
    suspend fun isUserInsideGeofence(latitude: Double, longitude: Double, radiusMeters: Double): GeofenceProximity
}
