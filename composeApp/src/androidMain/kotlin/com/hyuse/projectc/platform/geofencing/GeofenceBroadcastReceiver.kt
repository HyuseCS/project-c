package com.hyuse.projectc.platform.geofencing

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.hyuse.projectc.domain.repository.ReminderRepository
import com.hyuse.projectc.domain.repository.ReminderScheduler
import com.hyuse.projectc.domain.usecase.EvaluateTriggerUseCase
import com.hyuse.projectc.domain.usecase.TriggerAction
import com.hyuse.projectc.domain.usecase.TriggerEvent
import com.hyuse.projectc.platform.notification.NotificationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GeofenceBroadcastReceiver : BroadcastReceiver(), KoinComponent {

    private val reminderRepository: ReminderRepository by inject()
    private val reminderScheduler: ReminderScheduler by inject()
    private val evaluateTrigger: EvaluateTriggerUseCase by inject()

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
                            val action = evaluateTrigger(reminder, now, TriggerEvent.GEOFENCE_ENTRY)
                            when (action) {
                                TriggerAction.DISPATCH_AUDIBLE -> {
                                    Log.d("GeofenceReceiver", "Triggering audible reminder: ${reminder.title}")
                                    NotificationHelper.showNotification(context, reminder, isAudible = true)
                                    reminderRepository.updateLastTriggered(reminder.id, now)
                                }
                                TriggerAction.DISPATCH_SILENT -> {
                                    // Arrived early - silent heads-up, and arm the time-based trigger.
                                    Log.d("GeofenceReceiver", "Early arrival, scheduling time trigger: ${reminder.title}")
                                    NotificationHelper.showNotification(context, reminder, isAudible = false)
                                    reminderScheduler.scheduleReminder(reminder)
                                }
                                else -> {
                                    Log.d("GeofenceReceiver", "Reminder ${reminder.title} skipped (cooldown or ignore)")
                                }
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
