package ru.practicum.android.diploma.filter.domain.models

import ru.practicum.android.diploma.search.domain.models.Errors

sealed interface FilterResult {
    data class Regions(
        val regions: List<Region>
    ) : FilterResult

    data class Industries(
        val industries: List<Industry>
    ) : FilterResult

    data class Error(
        val error: Errors
    ) : FilterResult
}
