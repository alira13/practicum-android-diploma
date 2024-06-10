package ru.practicum.android.diploma.search.di

import org.koin.dsl.module
import ru.practicum.android.diploma.search.data.Converter
import ru.practicum.android.diploma.search.data.impl.SearchRepositoryImpl
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.api.SearchRepository
import ru.practicum.android.diploma.search.domain.impl.SearchInteractorImpl

val searchRepositoryModule = module {

    factory { Converter() }

    single<SearchRepository> {
        SearchRepositoryImpl(get(), get())
    }

    single<SearchInteractor> {
        SearchInteractorImpl(get())
    }

}
