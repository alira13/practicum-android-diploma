package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.vacancy.data.DetailsConverter
import ru.practicum.android.diploma.vacancy.data.impl.VacancyDetailsRepositoryImpl
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailsInteractor
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailsRepository
import ru.practicum.android.diploma.vacancy.domain.impl.VacancyDetailsInteractorImpl

val vacancyDetailsRepositoryModule = module {
    factory {
        DetailsConverter(get())
    }
    single<VacancyDetailsRepository> {
        VacancyDetailsRepositoryImpl(get(), get())
    }
    single<VacancyDetailsInteractor> {
        VacancyDetailsInteractorImpl(get())
    }
}
