package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.favoritedetails.presentation.FavoriteDetailsViewModel
import ru.practicum.android.diploma.favorites.presentation.FavoritesViewModel
import ru.practicum.android.diploma.filter.presentation.FilterCountryViewModel
import ru.practicum.android.diploma.filter.presentation.FilterIndustryViewModel
import ru.practicum.android.diploma.filter.presentation.FilterLocationViewModel
import ru.practicum.android.diploma.filter.presentation.FilterSettingsViewModel
import ru.practicum.android.diploma.filter.presentation.FilterRegionViewModel
import ru.practicum.android.diploma.search.presentation.SearchVacanciesViewModel
import ru.practicum.android.diploma.vacancy.presentation.VacancyDetailsViewModel

val viewModelModule = module {

    viewModel {
        FilterLocationViewModel(get())
    }

    viewModel {
        FavoritesViewModel(
            interactor = get()
        )
    }

    viewModel {
        SearchVacanciesViewModel(
            searchInteractor = get(),
            settingsInteractor = get()
        )
    }

    viewModel { params ->
        VacancyDetailsViewModel(
            id = params.get(),
            vacancyDetailsInteractor = get(),
            sharingInteractor = get(),
            favoriteInteractor = get(),
        )
    }

    viewModel { (favoriteId: String) ->
        FavoriteDetailsViewModel(
            id = favoriteId,
            vacancyDetailsInteractor = get(),
            sharingInteractor = get(),
            favoriteInteractor = get()
        )
    }

    viewModel {
        FilterCountryViewModel(
            filterInteractor = get(),
            settingsInteractor = get()
        )
    }

    viewModel {
        FilterSettingsViewModel(
            settingsInteractor = get()
        )
    }

    viewModel {
        FilterRegionViewModel(
            interactor = get(),
            settingsInteractor = get()
        )
    }

    viewModel {
        FilterIndustryViewModel(
            settingsInteractor = get(),
            filterInteractor = get()
        )
    }
}
