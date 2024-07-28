package com.example.orbitmvi_sample

import android.app.Application
import com.example.orbitmvi_sample.di.appModule
import org.koin.core.context.startKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(appModule)
        }
    }
}