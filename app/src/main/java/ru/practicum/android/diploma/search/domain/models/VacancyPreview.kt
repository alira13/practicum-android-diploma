package ru.practicum.android.diploma.search.domain.models

import ru.practicum.android.diploma.search.ui.models.ViewType

data class VacancyPreview(
    val id: String,
    val iconUrl: String?,
    val description: String,
    val employer: String,
    val salary: String
) : ViewType()
