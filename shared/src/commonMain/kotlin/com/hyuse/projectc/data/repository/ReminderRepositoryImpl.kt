package com.hyuse.projectc.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.hyuse.projectc.domain.model.LocationData
import com.hyuse.projectc.domain.model.Reminder
import com.hyuse.projectc.domain.model.ReminderImportance
import com.hyuse.projectc.domain.repository.ReminderRepository
import com.hyuse.projectc.shared.database.ProjectCDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ReminderRepositoryImpl(
    private val database: ProjectCDatabase
) : ReminderRepository {
    private val queries = database.reminderEntityQueries

    override fun observeAllReminders(): Flow<List<Reminder>> {
        return queries.getAllReminders()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { entities ->
                entities.map { entity ->
                    val location = if (entity.latitude != null && entity.longitude != null && entity.radius != null) {
                        LocationData(
                            latitude = entity.latitude,
                            longitude = entity.longitude,
                            radius = entity.radius
                        )
                    } else null

                    Reminder(
                        id = entity.id,
                        title = entity.title,
                        description = entity.description,
                        dateMillis = entity.dateMillis,
                        timeMillis = entity.timeMillis,
                        importance = ReminderImportance.valueOf(entity.importance),
                        location = location,
                        lastTriggeredMillis = entity.lastTriggeredMillis,
                        geofenceId = entity.geofenceId
                    )
                }
            }
    }

    override suspend fun getAllReminders(): List<Reminder> = withContext(Dispatchers.Default) {
        queries.getAllReminders().executeAsList().map { entity ->
            val location = if (entity.latitude != null && entity.longitude != null && entity.radius != null) {
                LocationData(
                    latitude = entity.latitude,
                    longitude = entity.longitude,
                    radius = entity.radius
                )
            } else null

            Reminder(
                id = entity.id,
                title = entity.title,
                description = entity.description,
                dateMillis = entity.dateMillis,
                timeMillis = entity.timeMillis,
                importance = ReminderImportance.valueOf(entity.importance),
                location = location,
                lastTriggeredMillis = entity.lastTriggeredMillis,
                geofenceId = entity.geofenceId
            )
        }
    }

    override suspend fun getReminderById(id: String): Reminder? = withContext(Dispatchers.Default) {
        queries.getReminderById(id).executeAsOneOrNull()?.let { entity ->
            val location = if (entity.latitude != null && entity.longitude != null && entity.radius != null) {
                LocationData(
                    latitude = entity.latitude,
                    longitude = entity.longitude,
                    radius = entity.radius
                )
            } else null

            Reminder(
                id = entity.id,
                title = entity.title,
                description = entity.description,
                dateMillis = entity.dateMillis,
                timeMillis = entity.timeMillis,
                importance = ReminderImportance.valueOf(entity.importance),
                location = location,
                lastTriggeredMillis = entity.lastTriggeredMillis,
                geofenceId = entity.geofenceId
            )
        }
    }

    override suspend fun saveReminder(reminder: Reminder) = withContext(Dispatchers.Default) {
        queries.insertReminder(
            id = reminder.id,
            title = reminder.title,
            description = reminder.description,
            dateMillis = reminder.dateMillis,
            timeMillis = reminder.timeMillis,
            importance = reminder.importance.name,
            latitude = reminder.location?.latitude,
            longitude = reminder.location?.longitude,
            radius = reminder.location?.radius,
            lastTriggeredMillis = reminder.lastTriggeredMillis,
            geofenceId = reminder.geofenceId
        )
    }

    override suspend fun deleteReminder(id: String) = withContext(Dispatchers.Default) {
        queries.deleteReminder(id)
    }

    override suspend fun updateLastTriggered(id: String, timestamp: Long) = withContext(Dispatchers.Default) {
        queries.updateLastTriggered(timestamp, id)
    }
}
