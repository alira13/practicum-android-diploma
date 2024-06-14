package ru.practicum.android.diploma.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.data.converters.VacancyConverterDB
import ru.practicum.android.diploma.favorites.data.db.AppDatabase
import ru.practicum.android.diploma.favorites.data.db.impl.VacancyRepositoryImpl
import ru.practicum.android.diploma.favorites.domain.api.VacancyRepository
import ru.practicum.android.diploma.favorites.domain.api.VacansyInteractor
import ru.practicum.android.diploma.favorites.domain.impl.VacancyInteractorImpl

val favoritesModule = module {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    single<VacancyRepository> {
        VacancyRepositoryImpl(get(), get())
    }

    single<VacansyInteractor> {
        VacancyInteractorImpl(get())
    }

    single { VacancyConverterDB() }
}
