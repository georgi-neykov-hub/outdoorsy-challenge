package com.neykov.outdoorsy.challenge

import androidx.lifecycle.SavedStateHandle
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.koinApplication
import org.koin.test.KoinTest
import org.koin.test.check.checkModules

@RunWith(AndroidJUnit4::class)
class DependencyGraphTest : KoinTest {

    @Test
    fun testDependencyGraph() {
        koinApplication {
            androidContext(ApplicationProvider.getApplicationContext())
            addAllModules()
        }.apply {
            createEagerInstances()
            checkModules {
                withInstance(SavedStateHandle())
            }
        }
    }
}