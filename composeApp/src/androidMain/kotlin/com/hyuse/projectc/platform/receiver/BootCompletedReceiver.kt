package com.hyuse.projectc.platform.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.hyuse.projectc.domain.repository.GeofenceManager
import com.hyuse.projectc.domain.repository.ReminderRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BootCompletedReceiver : BroadcastReceiver(), KoinComponent {

    private val reminderRepository: ReminderRepository by inject()
    private val geofenceManager: GeofenceManager by inject()

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d("BootCompletedReceiver", "Device booted. Re-registering geofences...")
            
            CoroutineScope(Dispatchers.IO).launch {
                val reminders = reminderRepository.getAllReminders()
                geofenceManager.registerGeofences(reminders)
            }
        }
    }
}
