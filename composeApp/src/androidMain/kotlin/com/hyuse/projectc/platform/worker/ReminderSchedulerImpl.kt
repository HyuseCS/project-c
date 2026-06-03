package com.hyuse.projectc.platform.worker

import android.content.Context
import androidx.work.*
import com.hyuse.projectc.domain.model.Reminder
import com.hyuse.projectc.domain.repository.ReminderScheduler
import java.util.concurrent.TimeUnit

class ReminderSchedulerImpl(private val context: Context) : ReminderScheduler {

    override fun scheduleReminder(reminder: Reminder) {
        val currentTime = System.currentTimeMillis()
        val triggerTime = reminder.dateMillis + reminder.timeMillis
        val delay = triggerTime - currentTime

        if (delay <= 0) return

        val workRequest = OneTimeWorkRequestBuilder<ReminderTimeWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(workDataOf("reminder_id" to reminder.id))
            .addTag("reminder_${reminder.id}")
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "reminder_${reminder.id}",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }

    override fun cancelReminder(reminderId: String) {
        WorkManager.getInstance(context).cancelUniqueWork("reminder_$reminderId")
    }
}
