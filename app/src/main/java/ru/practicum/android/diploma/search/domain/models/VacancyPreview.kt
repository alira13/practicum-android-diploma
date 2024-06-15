package ru.practicum.android.diploma.search.domain.models

data class VacancyPreview(
    val id: String,
    val iconUrl: String?,
    val description: String,
    val employer: String?,
    val salary: String?
)
