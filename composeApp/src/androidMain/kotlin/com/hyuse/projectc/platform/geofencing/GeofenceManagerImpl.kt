package com.hyuse.projectc.platform.geofencing

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.GeofenceStatusCodes
import com.hyuse.projectc.domain.model.Reminder
import com.hyuse.projectc.domain.repository.GeofenceManager
import kotlinx.coroutines.tasks.await

class GeofenceManagerImpl(private val context: Context) : GeofenceManager {

    private val geofencingClient: GeofencingClient = LocationServices.getGeofencingClient(context)

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back
        // FLAG_MUTABLE is required if we want the system to fill in the intent with geofence transition data
        PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
    }

    @SuppressLint("MissingPermission")
    override suspend fun registerGeofences(reminders: List<Reminder>) {
        if (reminders.isEmpty()) {
            unregisterAllGeofences()
            return
        }

        val locationReminders = reminders.filter { it.location != null && it.geofenceId != null }
        if (locationReminders.isEmpty()) {
            unregisterAllGeofences()
            return
        }

        // Play Services limit is 100 geofences per app.
        // The project requirements cap at 90 (3 for free tier).
        val geofencesToRegister = locationReminders.take(90).map { reminder ->
            val location = reminder.location!!
            
            // Calculate expiration based on dateMillis if timeMillis is specified and it's a specific date,
            // otherwise use NEVER_EXPIRE.
            val expiration = if (reminder.dateMillis > 0 && reminder.timeMillis > 0) {
                // Approximate expiration as end of the day or just some duration after the intended time.
                // For simplicity and robustness, we can set NEVER_EXPIRE and manually clean up when the user
                // dismisses or deletes it, but a robust app cleans up. We will use NEVER_EXPIRE here
                // to rely on our DB source of truth for cleanup via deleteReminder.
                Geofence.NEVER_EXPIRE
            } else {
                Geofence.NEVER_EXPIRE
            }

            Geofence.Builder()
                .setRequestId(reminder.geofenceId!!)
                .setCircularRegion(
                    location.latitude,
                    location.longitude,
                    location.radius.toFloat()
                )
                .setExpirationDuration(expiration)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_DWELL)
                .setLoiteringDelay(30000) // 30 seconds of loitering to avoid false triggers while driving by
                .build()
        }

        val geofencingRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER or GeofencingRequest.INITIAL_TRIGGER_DWELL)
            .addGeofences(geofencesToRegister)
            .build()

        try {
            geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent).await()
            Log.d("GeofenceManager", "Successfully registered ${geofencesToRegister.size} geofences")
        } catch (e: Exception) {
            Log.e("GeofenceManager", "Failed to register geofences", e)
            if (e is ApiException) {
                if (e.statusCode == GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES) {
                    Log.e("GeofenceManager", "Exceeded Play Services maximum of 100 geofences")
                }
            }
        }
    }

    override suspend fun unregisterGeofence(geofenceId: String) {
        try {
            geofencingClient.removeGeofences(listOf(geofenceId)).await()
            Log.d("GeofenceManager", "Successfully unregistered geofence $geofenceId")
        } catch (e: Exception) {
            Log.e("GeofenceManager", "Failed to unregister geofence $geofenceId", e)
        }
    }

    override suspend fun unregisterAllGeofences() {
        try {
            geofencingClient.removeGeofences(geofencePendingIntent).await()
            Log.d("GeofenceManager", "Successfully unregistered all geofences")
        } catch (e: Exception) {
            Log.e("GeofenceManager", "Failed to unregister all geofences", e)
        }
    }
}
