package com.hyuse.projectc.domain.usecase

import com.hyuse.projectc.domain.model.Reminder

enum class TriggerEvent {
    GEOFENCE_ENTRY,
    ALARM_FIRED
}

enum class TriggerAction {
    SCHEDULE_ALARM,
    DISPATCH_SILENT,
    DISPATCH_AUDIBLE,
    IGNORE
}

class EvaluateTriggerUseCase {
    operator fun invoke(
        reminder: Reminder,
        currentTimeMillis: Long,
        currentEvent: TriggerEvent
    ): TriggerAction {
        val reminderTime = reminder.dateMillis + reminder.timeMillis
        val isLateOrOnTime = currentTimeMillis >= reminderTime
        
        // Cooldown check (e.g., 5 minutes) to prevent flapping
        val cooldownMillis = 5 * 60 * 1000L
        val lastTriggered = reminder.lastTriggeredMillis ?: 0L
        val isCoolingDown = (currentTimeMillis - lastTriggered) < cooldownMillis
        
        if (isCoolingDown) {
            return TriggerAction.IGNORE
        }

        return when (currentEvent) {
            TriggerEvent.GEOFENCE_ENTRY -> {
                if (isLateOrOnTime) {
                    TriggerAction.DISPATCH_AUDIBLE
                } else {
                    TriggerAction.DISPATCH_SILENT // And caller must schedule alarm
                }
            }
            TriggerEvent.ALARM_FIRED -> {
                // We only receive an ALARM_FIRED if we scheduled it during a GEOFENCE_ENTRY,
                // and the user is still in the geofence (the worker verifies location before calling this).
                // Or if it's a generic time trigger and we are in the geofence.
                TriggerAction.DISPATCH_AUDIBLE
            }
        }
    }
}
