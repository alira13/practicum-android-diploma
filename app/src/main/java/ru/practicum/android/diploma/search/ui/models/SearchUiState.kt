package ru.practicum.android.diploma.search.ui.models

import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.search.domain.models.Errors
import ru.practicum.android.diploma.search.domain.models.VacancyPreview

sealed class SearchUiState(
    open val clearEnabled: Boolean = true,
    open val clearIcon: Int = R.drawable.ic_close,
    open val countIsVisible: Boolean = false,
    open val placeholderImageIsVisible: Boolean = false,
    open val placeholderImageIcon: Int? = null,
    open val placeholderMessageIsVisible: Boolean = false,
    open val vacanciesListRvIsVisible: Boolean = false,
    open val progressBarIsVisible: Boolean = false
) {
    data class Default(
        override val clearEnabled: Boolean = false,
        override val clearIcon: Int = R.drawable.ic_search,
        override val placeholderImageIsVisible: Boolean = true,
        override val placeholderImageIcon: Int? = R.drawable.placeholder_main
    ) : SearchUiState()

    data object EditingRequest : SearchUiState()

    data class Loading(
        override val progressBarIsVisible: Boolean = true
    ) : SearchUiState()

    data class PagingLoading(
        override val countIsVisible: Boolean = true,
        override val vacanciesListRvIsVisible: Boolean = true
    ) : SearchUiState()

    data class SearchResult(
        override val countIsVisible: Boolean = true,
        override val vacanciesListRvIsVisible: Boolean = true,
        val vacancies: List<VacancyPreview>,
        val count: String,
        val isItFirstPage: Boolean
    ) : SearchUiState()

    data class EmptyResult(
        override val placeholderImageIsVisible: Boolean = true,
        override val placeholderImageIcon: Int? = R.drawable.placeholder_error,
        override val placeholderMessageIsVisible: Boolean = true,
        override val countIsVisible: Boolean = true
    ) : SearchUiState()

    data class FirstRequestError(
        override val placeholderImageIsVisible: Boolean = true,
        override val placeholderImageIcon: Int? = R.drawable.placeholder_internet_error,
        override val placeholderMessageIsVisible: Boolean = true,
        val error: Errors
    ) : SearchUiState()

    data class PagingError(
        override val countIsVisible: Boolean = true,
        override val vacanciesListRvIsVisible: Boolean = true,
        val error: Errors
    ) : SearchUiState()

    data class FullLoaded(
        override val countIsVisible: Boolean = true,
        override val vacanciesListRvIsVisible: Boolean = true
    ) : SearchUiState()
}
