package com.hyuse.projectc.domain.repository

import com.hyuse.projectc.domain.model.Reminder
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {
    fun observeAllReminders(): Flow<List<Reminder>>
    suspend fun getAllReminders(): List<Reminder>
    suspend fun getReminderById(id: String): Reminder?
    suspend fun saveReminder(reminder: Reminder)
    suspend fun deleteReminder(id: String)
    suspend fun updateLastTriggered(id: String, timestamp: Long)
}
