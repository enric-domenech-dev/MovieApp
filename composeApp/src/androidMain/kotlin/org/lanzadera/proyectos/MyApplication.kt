package org.lanzadera.proyectos

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level
import org.lanzadera.proyectos.di.appModule
import org.lanzadera.proyectos.di.initKoin
import org.lanzadera.proyectos.utils.AppContextProvider

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        AppContextProvider.init(this)
        initKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MyApplication)
            modules(appModule)
        }
    }
}