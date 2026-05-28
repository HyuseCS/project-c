package com.hyuse.projectc.data.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.hyuse.projectc.shared.database.ProjectCDatabase

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(ProjectCDatabase.Schema, "projectc.db")
    }
}
