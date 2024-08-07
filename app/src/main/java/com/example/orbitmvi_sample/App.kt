package com.example.orbitmvi_sample

import android.app.Application
import com.example.orbitmvi_sample.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        GlobalContext.startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }
}