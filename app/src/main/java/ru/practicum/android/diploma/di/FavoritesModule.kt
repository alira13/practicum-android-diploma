package ru.practicum.android.diploma.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.data.converters.FavoriteConverter
import ru.practicum.android.diploma.favorites.data.db.AppDatabase
import ru.practicum.android.diploma.favorites.data.db.impl.FavoriteRepositoryImpl
import ru.practicum.android.diploma.favorites.domain.api.FavoriteRepository
import ru.practicum.android.diploma.favorites.domain.api.FavoriteInteractor
import ru.practicum.android.diploma.favorites.domain.impl.VacancyInteractorImpl

val favoritesModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "database.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    single<FavoriteRepository> {
        FavoriteRepositoryImpl(get(), get())
    }

    single<FavoriteInteractor> {
        VacancyInteractorImpl(get())
    }

    single { FavoriteConverter() }
}
