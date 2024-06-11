package ru.practicum.android.diploma.search.domain.models

data class VacanciesSearchRequest(
    val page: Int,
    val searchString: String
)
