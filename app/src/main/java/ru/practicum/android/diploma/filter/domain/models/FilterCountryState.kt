package ru.practicum.android.diploma.filter.domain.models

sealed interface FilterCountryState {

    data class Content(
        val countries: List<Region>
    ) : FilterCountryState

    object Error : FilterCountryState

    object Default : FilterCountryState
}
