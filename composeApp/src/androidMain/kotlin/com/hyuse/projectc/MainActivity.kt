package com.hyuse.projectc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.hyuse.projectc.domain.repository.GeofenceManager
import com.hyuse.projectc.domain.repository.ReminderRepository
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val reminderRepository: ReminderRepository by inject()
    private val geofenceManager: GeofenceManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Safety net: Re-register geofences to handle cases where Play Services 
        // updates or force-stops cleared them out.
        lifecycleScope.launch {
            try {
                val reminders = reminderRepository.getAllReminders()
                geofenceManager.registerGeofences(reminders)
            } catch (e: Exception) {
                // Ignore
            }
        }

        setContent {
            App()
        }
    }
}