package ru.practicum.android.diploma.search.ui.models

import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.search.domain.models.Errors
import ru.practicum.android.diploma.search.domain.models.VacancyPreview

sealed class SearchUiState(
    open var clearEnabled: Boolean = true,
    open var clearIcon: Int = R.drawable.ic_close,
    open var countIsVisible: Boolean = false,
    open var placeholderImageIsVisible: Boolean = false,
    open var placeholderImageIcon: Int? = null,
    open var placeholderMessageIsVisible: Boolean = false,
    open var vacanciesListRvIsVisible: Boolean = false,
    open var progressBarIsVisible: Boolean = false,
    open var progressBarPgIsVisible: Boolean = false,
    ) {
    data class Default(
        override var clearEnabled: Boolean = false,
        override var clearIcon: Int = R.drawable.ic_search,
        override var placeholderImageIsVisible: Boolean = true,
        override var placeholderImageIcon: Int? = R.drawable.placeholder_main
        ): SearchUiState()

    data object EditingRequest: SearchUiState()

    data class Loading(
        override var progressBarIsVisible: Boolean = true
    ): SearchUiState()

    data class PagingLoading(
        override var countIsVisible: Boolean = true,
        override var vacanciesListRvIsVisible: Boolean = true,
        override var progressBarPgIsVisible: Boolean = true
    ): SearchUiState()

    data class SearchResult(
        override var countIsVisible: Boolean = true,
        override var vacanciesListRvIsVisible: Boolean = true,
        val vacancies: List<VacancyPreview>,
        val count: String,
        val isItFirstPage: Boolean
    ) : SearchUiState()

    data class EmptyResult(
        override var placeholderImageIsVisible: Boolean = true,
        override var placeholderImageIcon: Int? = R.drawable.placeholder_error,
        override var placeholderMessageIsVisible: Boolean = true,
        override var countIsVisible: Boolean = true
    ): SearchUiState()

    data class FirstRequestError(
        override var placeholderImageIsVisible: Boolean = true,
        override var placeholderImageIcon: Int? = R.drawable.placeholder_internet_error,
        override var placeholderMessageIsVisible: Boolean = true,
        val error: Errors
    ): SearchUiState()

    data class PagingError(
        override var countIsVisible: Boolean = true,
        override var vacanciesListRvIsVisible: Boolean = true,
        val error: Errors
    ): SearchUiState()

    data class FullLoaded(
        override var countIsVisible: Boolean = true,
        override var vacanciesListRvIsVisible: Boolean = true,
        override var progressBarPgIsVisible: Boolean = true
    ): SearchUiState()
}
