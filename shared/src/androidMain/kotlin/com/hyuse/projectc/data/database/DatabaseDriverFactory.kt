package com.hyuse.projectc.data.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import android.content.Context
import com.hyuse.projectc.shared.database.ProjectCDatabase

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(ProjectCDatabase.Schema, context, "projectc.db")
    }
}
