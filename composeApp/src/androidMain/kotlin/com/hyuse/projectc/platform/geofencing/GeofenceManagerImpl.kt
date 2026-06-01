package com.hyuse.projectc.platform.geofencing

import android.content.Context
import android.util.Log
import com.hyuse.projectc.domain.model.Reminder
import com.hyuse.projectc.domain.repository.GeofenceManager

class GeofenceManagerImpl(private val context: Context) : GeofenceManager {

    override suspend fun registerGeofences(reminders: List<Reminder>) {
        if (reminders.isEmpty()) {
            unregisterAllGeofences()
            return
        }
        Log.d("GeofenceManager", "Starting custom location service for ${reminders.size} reminders")
        CustomLocationService.start(context)
    }

    override suspend fun unregisterGeofence(geofenceId: String) {
        // With a single service monitoring all, we don't necessarily stop the service
        // unless there are zero reminders left. But the service handles re-fetching.
        Log.d("GeofenceManager", "Unregistering geofence $geofenceId (Service remains active)")
    }

    override suspend fun unregisterAllGeofences() {
        Log.d("GeofenceManager", "Stopping custom location service")
        CustomLocationService.stop(context)
    }
}
