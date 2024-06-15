package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.presentation.FavoritesViewModel
import ru.practicum.android.diploma.search.presentation.SearchVacanciesViewModel
import ru.practicum.android.diploma.vacancy.presentation.VacancyDetailsViewModel

val viewModelModule = module {

    viewModel {
        FavoritesViewModel(
            interactor = get()
        )
    }

    viewModel {
        SearchVacanciesViewModel(
            searchInteractor = get()
        )
    }

    viewModel { params ->
        VacancyDetailsViewModel(
            id = params.get(),
            vacancyDetailsInteractor = get(),
            sharingInteractor = get()
        )
    }
}
