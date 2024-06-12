package ru.practicum.android.diploma.favorites.presentation

import ru.practicum.android.diploma.search.domain.models.VacancyPreview

sealed interface FavoritesUiState {
    data object Default : FavoritesUiState
    data object Empty : FavoritesUiState
    data object Failure : FavoritesUiState
    data class Content(val list: List<VacancyPreview>) : FavoritesUiState
}
