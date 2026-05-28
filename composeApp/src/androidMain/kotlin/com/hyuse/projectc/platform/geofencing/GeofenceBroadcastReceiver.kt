package com.hyuse.projectc.platform.geofencing

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import com.hyuse.projectc.domain.repository.ReminderRepository
import com.hyuse.projectc.domain.usecase.EvaluateTriggerUseCase
import com.hyuse.projectc.domain.usecase.TriggerAction
import com.hyuse.projectc.domain.usecase.TriggerEvent
import com.hyuse.projectc.platform.worker.ReminderTimeWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit

class GeofenceBroadcastReceiver : BroadcastReceiver(), KoinComponent {

    private val reminderRepository: ReminderRepository by inject()
    private val evaluateTrigger: EvaluateTriggerUseCase by inject()

    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent == null || geofencingEvent.hasError()) {
            val errorMessage = GeofenceStatusCodes.getStatusCodeString(
                geofencingEvent?.errorCode ?: GeofenceStatusCodes.ERROR
            )
            Log.e("GeofenceReceiver", "Geofencing error: $errorMessage")
            return
        }

        val geofenceTransition = geofencingEvent.geofenceTransition
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            val triggeringGeofences = geofencingEvent.triggeringGeofences ?: return

            CoroutineScope(Dispatchers.IO).launch {
                val currentTime = System.currentTimeMillis()
                val allReminders = reminderRepository.getAllReminders()

                for (geofence in triggeringGeofences) {
                    // Find all reminders tied to this geofence
                    val matchedReminders = allReminders.filter { it.geofenceId == geofence.requestId }

                    for (reminder in matchedReminders) {
                        val action = evaluateTrigger(reminder, currentTime, TriggerEvent.GEOFENCE_ENTRY)
                        
                        when (action) {
                            TriggerAction.DISPATCH_AUDIBLE -> {
                                dispatchNotification(context, reminder, isAudible = true)
                                reminderRepository.updateLastTriggered(reminder.id, currentTime)
                            }
                            TriggerAction.DISPATCH_SILENT -> {
                                dispatchNotification(context, reminder, isAudible = false)
                                scheduleTimeWorker(context, reminder)
                                reminderRepository.updateLastTriggered(reminder.id, currentTime)
                            }
                            TriggerAction.IGNORE, TriggerAction.SCHEDULE_ALARM -> {
                                // Do nothing for ignore. SCHEDULE_ALARM is basically handled above via DISPATCH_SILENT logic
                            }
                        }
                    }
                }
            }
        }
    }

    private fun dispatchNotification(context: Context, reminder: com.hyuse.projectc.domain.model.Reminder, isAudible: Boolean) {
        // TODO: Implement notification dispatch using NotificationManagerCompat
        Log.d("GeofenceReceiver", "Dispatching notification for ${reminder.title}, audible=$isAudible")
    }

    private fun scheduleTimeWorker(context: Context, reminder: com.hyuse.projectc.domain.model.Reminder) {
        val reminderTime = reminder.dateMillis + reminder.timeMillis
        val delayMillis = reminderTime - System.currentTimeMillis()
        
        if (delayMillis <= 0) return

        val data = Data.Builder()
            .putString("reminder_id", reminder.id)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<ReminderTimeWorker>()
            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }
}
