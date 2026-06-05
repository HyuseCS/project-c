package com.hyuse.projectc.domain.repository

import com.hyuse.projectc.domain.model.Reminder

interface ReminderScheduler {
    fun scheduleReminder(reminder: Reminder)
    fun cancelReminder(reminderId: String)
}
