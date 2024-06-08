package ru.practicum.android.diploma.search.domain.models

sealed class Request(
    open val page: Int
) {
    data class VacanciesSearch(
        override val page: Int,
        val searchString: String
    ) : Request(page)
}
