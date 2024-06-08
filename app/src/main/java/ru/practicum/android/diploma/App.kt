package ru.practicum.android.diploma

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.practicum.android.diploma.di.networkClientModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        startKoin {
            androidContext(this@App)
            modules(
                networkClientModule
            )
        }
    }

    companion object {
        var INSTANCE: App? = null
    }
}
