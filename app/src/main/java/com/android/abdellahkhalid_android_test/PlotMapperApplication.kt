package com.android.abdellahkhalid_android_test

import android.app.Application
import com.android.abdellahkhalid_android_test.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PlotMapperApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidContext(this@PlotMapperApplication)
            modules(appModule)
        }
    }
}
