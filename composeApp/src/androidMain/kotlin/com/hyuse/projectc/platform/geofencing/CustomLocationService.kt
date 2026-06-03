package com.hyuse.projectc.platform.geofencing

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import com.hyuse.projectc.domain.repository.ReminderRepository
import com.hyuse.projectc.platform.notification.NotificationHelper
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject

class CustomLocationService : Service() {

    private val reminderRepository: ReminderRepository by inject()
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var locationManager: LocationManager? = null

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            Log.d("LocationService", "Location update: ${location.latitude}, ${location.longitude}")
            evaluateReminders(location)
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    override fun onCreate() {
        super.onCreate()
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    @SuppressLint("MissingPermission")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = NotificationHelper.createServiceNotification(this)
        startForeground(NotificationHelper.SERVICE_NOTIFICATION_ID, notification)

        // Request updates every 2 minutes or 50 meters to balance battery
        try {
            locationManager?.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                120000L,
                50f,
                locationListener
            )
            locationManager?.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                120000L,
                50f,
                locationListener
            )
        } catch (e: Exception) {
            Log.e("LocationService", "Failed to request location updates", e)
        }

        return START_STICKY
    }

    private fun evaluateReminders(currentLocation: Location) {
        serviceScope.launch {
            val reminders = reminderRepository.getAllReminders()
            reminders.forEach { reminder ->
                val loc = reminder.location ?: return@forEach
                val results = FloatArray(1)
                Location.distanceBetween(
                    currentLocation.latitude,
                    currentLocation.longitude,
                    loc.latitude,
                    loc.longitude,
                    results
                )
                val distance = results[0]

                if (distance <= loc.radius) {
                    // Check if already triggered recently (within last 10 mins) to avoid spam
                    val now = System.currentTimeMillis()
                    val lastTriggered = reminder.lastTriggeredMillis ?: 0L
                    if (now - lastTriggered > 600000L) {
                        Log.d("LocationService", "Triggering reminder: ${reminder.title}")
                        NotificationHelper.showNotification(this@CustomLocationService, reminder, isAudible = true)
                        reminderRepository.updateLastTriggered(reminder.id, now)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        locationManager?.removeUpdates(locationListener)
        serviceScope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, CustomLocationService::class.java)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stop(context: Context) {
            val intent = Intent(context, CustomLocationService::class.java)
            context.stopService(intent)
        }
    }
}
