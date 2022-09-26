package com.breno.gitviewer

import android.app.Application
import com.breno.gitviewer.di.moduleNetwork
import com.breno.gitviewer.di.moduleViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            loadKoinModules(
                listOf(
                    moduleNetwork, moduleViewModel
                )
            )
        }
    }
}