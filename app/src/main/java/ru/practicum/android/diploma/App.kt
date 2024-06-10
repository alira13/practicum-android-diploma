package ru.practicum.android.diploma

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.practicum.android.diploma.di.favoritesModule
import ru.practicum.android.diploma.di.networkClientModule
import ru.practicum.android.diploma.search.di.searchRepositoryModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        startKoin {
            androidContext(this@App)
            modules(
                networkClientModule,
                searchRepositoryModule,

                favoritesModule,
            )
        }
    }

    companion object {
        var INSTANCE: App? = null
    }
}
