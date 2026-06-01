package com.hyuse.projectc.platform.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.hyuse.projectc.domain.model.Reminder

object NotificationHelper {
    private const val CHANNEL_ID = "reminders_channel"
    private const val CHANNEL_NAME = "Location Reminders"
    private const val CHANNEL_DESCRIPTION = "Notifications for location-based tasks"

    private const val SERVICE_CHANNEL_ID = "location_service_channel"
    private const val SERVICE_CHANNEL_NAME = "Location Tracking Service"
    const val SERVICE_NOTIFICATION_ID = 1001

    fun showNotification(context: Context, reminder: Reminder, isAudible: Boolean) {
        createNotificationChannel(context)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // System default icon
            .setContentTitle(reminder.title)
            .setContentText(reminder.description)
            .setPriority(if (isAudible) NotificationCompat.PRIORITY_HIGH else NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        if (isAudible) {
            builder.setDefaults(NotificationCompat.DEFAULT_ALL)
        }

        try {
            NotificationManagerCompat.from(context).notify(reminder.id.hashCode(), builder.build())
        } catch (e: SecurityException) {
            // Permission not granted for POST_NOTIFICATIONS on Android 13+
        }
    }

    fun createServiceNotification(context: Context): android.app.Notification {
        createServiceChannel(context)
        return NotificationCompat.Builder(context, SERVICE_CHANNEL_ID)
            .setContentTitle("ProjectC Location Service")
            .setContentText("Monitoring your active reminders...")
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
    }

    private fun createServiceChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                SERVICE_CHANNEL_ID,
                SERVICE_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESCRIPTION
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
