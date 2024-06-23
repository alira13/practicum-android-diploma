package ru.practicum.android.diploma.search.domain.models

import ru.practicum.android.diploma.filter.domain.models.Settings

data class VacanciesSearchRequest(
    val page: Int,
    val searchString: String,
    val filterSettings: Settings
)
