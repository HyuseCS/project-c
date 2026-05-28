package com.hyuse.projectc.platform.geofencing

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.hyuse.projectc.domain.model.Reminder
import com.hyuse.projectc.domain.repository.GeofenceManager
import kotlinx.coroutines.tasks.await

class GeofenceManagerImpl(private val context: Context) : GeofenceManager {

    private val geofencingClient = LocationServices.getGeofencingClient(context)

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
    }

    @SuppressLint("MissingPermission")
    override suspend fun registerGeofences(reminders: List<Reminder>) {
        if (reminders.isEmpty()) return

        // 1. Group reminders by geofenceId to avoid duplicate geofences at same location
        val uniqueGeofences = reminders.distinctBy { it.geofenceId }
        
        // 2. Map to Google Play Services Geofence objects
        // Take top 90 to respect OS limits (in a real scenario, sort by distance/time)
        val geofenceList = uniqueGeofences.take(90).map { reminder ->
            Geofence.Builder()
                .setRequestId(reminder.geofenceId)
                .setCircularRegion(
                    reminder.location.latitude,
                    reminder.location.longitude,
                    reminder.location.radius.toFloat()
                )
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
                .build()
        }

        val geofencingRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofences(geofenceList)
            .build()

        try {
            geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent).await()
            Log.d("GeofenceManager", "Registered ${geofenceList.size} geofences")
        } catch (e: Exception) {
            Log.e("GeofenceManager", "Failed to register geofences", e)
        }
    }

    override suspend fun unregisterGeofence(geofenceId: String) {
        try {
            geofencingClient.removeGeofences(listOf(geofenceId)).await()
        } catch (e: Exception) {
            Log.e("GeofenceManager", "Failed to remove geofence $geofenceId", e)
        }
    }

    override suspend fun unregisterAllGeofences() {
        try {
            geofencingClient.removeGeofences(geofencePendingIntent).await()
        } catch (e: Exception) {
            Log.e("GeofenceManager", "Failed to remove all geofences", e)
        }
    }
}
