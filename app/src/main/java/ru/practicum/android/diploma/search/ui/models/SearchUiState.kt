package ru.practicum.android.diploma.search.ui.models

import ru.practicum.android.diploma.search.domain.models.Errors
import ru.practicum.android.diploma.search.domain.models.VacancyPreview

sealed interface SearchUiState {
    object Default : SearchUiState
    object EdittingRequest : SearchUiState
    object Loading : SearchUiState
    data class SearchResult(val vacancies: List<VacancyPreview>) : SearchUiState
    object EmptyResult : SearchUiState
    data class Error(val error: Errors) : SearchUiState
    object FullLoaded : SearchUiState
}
