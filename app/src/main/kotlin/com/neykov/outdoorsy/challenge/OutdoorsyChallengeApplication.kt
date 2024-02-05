package com.neykov.outdoorsy.challenge

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * Main entry point for the application used for setting-up the DI graph
 */
class OutdoorsyChallengeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@OutdoorsyChallengeApplication)
            allowOverride(false)
            addAllModules()
            androidLogger(Level.ERROR)
        }.apply {
            createEagerInstances()
        }
    }
}