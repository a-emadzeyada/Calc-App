package com.example.calcapp

import android.app.Application
import com.example.calcapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CalcApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@CalcApp)
            modules(appModule)
        }
    }
}


