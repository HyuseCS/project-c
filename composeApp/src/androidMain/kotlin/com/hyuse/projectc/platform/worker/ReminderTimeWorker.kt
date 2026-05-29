package com.hyuse.projectc.platform.worker

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.hyuse.projectc.domain.repository.ReminderRepository
import com.hyuse.projectc.domain.usecase.EvaluateTriggerUseCase
import com.hyuse.projectc.domain.usecase.TriggerAction
import com.hyuse.projectc.domain.usecase.TriggerEvent
import com.hyuse.projectc.platform.notification.NotificationHelper
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ReminderTimeWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {

    private val reminderRepository: ReminderRepository by inject()
    private val evaluateTrigger: EvaluateTriggerUseCase by inject()

    @SuppressLint("MissingPermission")
    override suspend fun doWork(): Result {
        val reminderId = inputData.getString("reminder_id") ?: return Result.failure()
        
        val reminder = reminderRepository.getReminderById(reminderId) ?: return Result.failure()
        val currentTime = System.currentTimeMillis()

        // We assume the user is still in the geofence because if they had exited, 
        // we should have cancelled this worker. (Cancellation logic would be added in EXIT transition).
        // For now, evaluate the trigger.
        val action = evaluateTrigger(reminder, currentTime, TriggerEvent.ALARM_FIRED)
        
        if (action == TriggerAction.DISPATCH_AUDIBLE) {
            NotificationHelper.showNotification(context, reminder, isAudible = true)
            reminderRepository.updateLastTriggered(reminder.id, currentTime)
            return Result.success()
        }

        return Result.success()
    }
}
