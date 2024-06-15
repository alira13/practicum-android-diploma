package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.search.presentation.SearchVacanciesViewModel
import ru.practicum.android.diploma.vacancy.presentation.VacancyDetailsViewModel

val viewModelModule = module {

    viewModel {
        SearchVacanciesViewModel(get())
    }

    viewModel { params ->
        VacancyDetailsViewModel(params.get(), get(), get(), get())
    }
}
