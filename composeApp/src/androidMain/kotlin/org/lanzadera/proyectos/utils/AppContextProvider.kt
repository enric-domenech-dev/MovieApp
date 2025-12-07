package org.lanzadera.proyectos.utils

import android.content.Context

object AppContextProvider {
    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    fun context(): Context =
        if (::appContext.isInitialized) appContext else error("AppContextProvider not initialized")
}

