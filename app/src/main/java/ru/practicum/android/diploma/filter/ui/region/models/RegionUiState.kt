package ru.practicum.android.diploma.filter.ui.region.models

import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.filter.domain.models.Region
import ru.practicum.android.diploma.search.domain.models.Errors

sealed class RegionUiState(
    open val clearEnabled: Boolean = true,
    open val clearIcon: Int = R.drawable.ic_close,
    open val placeholderImageIsVisible: Boolean = false,
    open val placeholderImageIcon: Int? = null,
    open val placeholderMessageIsVisible: Boolean = false,
    open val listRvIsVisible: Boolean = false,
    open val progressBarIsVisible: Boolean = false,
    open val dataToBeResumed: Boolean = false
) {
    data class Default(
        override val clearEnabled: Boolean = false,
        override val clearIcon: Int = R.drawable.ic_search,
        override val placeholderImageIsVisible: Boolean = true,
        override val placeholderImageIcon: Int? = R.drawable.placeholder_main
    ) : RegionUiState()

    data object EditingRequest : RegionUiState()

    data class Loading(
        override val progressBarIsVisible: Boolean = true
    ) : RegionUiState()

    data class PagingLoading(
        override val listRvIsVisible: Boolean = true
    ) : RegionUiState()

    data class SearchResult(
        override val listRvIsVisible: Boolean = true,
        val content: List<Region>,
        val count: String,
        val isItFirstPage: Boolean,
        val isFullLoaded: Boolean
    ) : RegionUiState()

    data class EmptyResult(
        override val placeholderImageIsVisible: Boolean = true,
        override val placeholderImageIcon: Int? = R.drawable.placeholder_error,
        override val placeholderMessageIsVisible: Boolean = true,
    ) : RegionUiState()

    data class FirstRequestError(
        override val placeholderImageIsVisible: Boolean = true,
        override val placeholderImageIcon: Int? = R.drawable.placeholder_empty_location_list,
        override val placeholderMessageIsVisible: Boolean = true,
        val error: Errors
    ) : RegionUiState()

    data class PagingError(
        override val listRvIsVisible: Boolean = true,
        override val dataToBeResumed: Boolean = true,
        val error: Errors
    ) : RegionUiState()
}
