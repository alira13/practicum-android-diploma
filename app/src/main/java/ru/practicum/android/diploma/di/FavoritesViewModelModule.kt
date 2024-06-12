package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.presentation.FavoritesViewModel
import ru.practicum.android.diploma.search.presentation.SearchVacanciesViewModel

val favoritesViewModelModule = module {

    viewModel {
        FavoritesViewModel()
    }
}
