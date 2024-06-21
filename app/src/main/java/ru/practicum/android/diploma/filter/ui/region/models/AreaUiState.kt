package ru.practicum.android.diploma.filter.ui.region.models

import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.filter.domain.models.Area
import ru.practicum.android.diploma.search.domain.models.Errors

sealed class AreaUiState(
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
        override val placeholderImageIsVisible: Boolean = false,
        override val listRvIsVisible: Boolean = true,
        val content: List<Area>
    ) : AreaUiState()

    data object EditingRequest : AreaUiState()

    data class Loading(
        override val progressBarIsVisible: Boolean = true
    ) : AreaUiState()

    data class SearchResult(
        override val listRvIsVisible: Boolean = true,
        val content: List<Area>
    ) : AreaUiState()

    data class EmptyResult(
        override val placeholderImageIsVisible: Boolean = true,
        override val placeholderImageIcon: Int? = R.drawable.placeholder_error,
        override val placeholderMessageIsVisible: Boolean = true,
    ) : AreaUiState()

    data class Error(
        override val placeholderImageIsVisible: Boolean = true,
        override val placeholderImageIcon: Int? = R.drawable.placeholder_empty_location_list,
        override val placeholderMessageIsVisible: Boolean = true,
        val error: Errors
    ) : AreaUiState()

}
