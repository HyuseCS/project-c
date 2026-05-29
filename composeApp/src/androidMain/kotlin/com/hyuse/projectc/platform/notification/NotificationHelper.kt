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
