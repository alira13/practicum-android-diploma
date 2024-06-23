package ru.practicum.android.diploma.filter.ui.model

import ru.practicum.android.diploma.filter.domain.models.Industry
import ru.practicum.android.diploma.search.domain.models.Errors

sealed interface FilterUIState {
    data object Loading : FilterUIState
    data class Content(
        val industries: List<Industry>
    ) : FilterUIState

    data class Error(
        val errors: Errors?
    ) : FilterUIState

    data object Empty : FilterUIState
}
