package com.hyuse.projectc

import android.app.Application
import com.hyuse.projectc.di.appModule
import com.hyuse.projectc.di.sharedModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * Custom Application class that initializes Koin DI at app startup.
 * Must be registered in AndroidManifest.xml via android:name=".ProjectCApplication"
 */
class ProjectCApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@ProjectCApplication)
            modules(sharedModule, appModule)
        }
    }
}
