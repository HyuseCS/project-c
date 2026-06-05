package com.hyuse.projectc.platform.geofencing

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.hyuse.projectc.domain.repository.ReminderRepository
import com.hyuse.projectc.platform.notification.NotificationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GeofenceBroadcastReceiver : BroadcastReceiver(), KoinComponent {

    private val reminderRepository: ReminderRepository by inject()

    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent) ?: return

        if (geofencingEvent.hasError()) {
            Log.e("GeofenceReceiver", "Geofencing error code: ${geofencingEvent.errorCode}")
            return
        }

        val geofenceTransition = geofencingEvent.geofenceTransition
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
            geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL) {

            val triggeringGeofences = geofencingEvent.triggeringGeofences ?: return
            
            // BroadcastReceivers have a short lifecycle (~10 seconds).
            // Since we need to query the database, we use goAsync() to keep the receiver alive.
            val pendingResult = goAsync()

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val allReminders = reminderRepository.getAllReminders()
                    val now = System.currentTimeMillis()

                    for (geofence in triggeringGeofences) {
                        val reminder = allReminders.find { it.geofenceId == geofence.requestId }
                        if (reminder != null) {
                            val lastTriggered = reminder.lastTriggeredMillis ?: 0L
                            // 10-minute debouncing cooldown
                            if (now - lastTriggered > 600000L) {
                                Log.d("GeofenceReceiver", "Triggering reminder: ${reminder.title}")
                                NotificationHelper.showNotification(context, reminder, isAudible = true)
                                reminderRepository.updateLastTriggered(reminder.id, now)
                            } else {
                                Log.d("GeofenceReceiver", "Reminder ${reminder.title} skipped due to debounce cooldown")
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("GeofenceReceiver", "Error processing geofence trigger", e)
                } finally {
                    pendingResult.finish()
                }
            }
        } else {
            Log.w("GeofenceReceiver", "Invalid transition type: $geofenceTransition")
        }
    }
}
