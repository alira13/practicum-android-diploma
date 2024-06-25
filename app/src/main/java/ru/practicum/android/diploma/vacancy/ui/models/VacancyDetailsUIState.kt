package ru.practicum.android.diploma.vacancy.ui.models

import ru.practicum.android.diploma.search.domain.models.Errors
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetails

sealed class VacancyDetailsUIState {
    data object Loading : VacancyDetailsUIState()
    data class Content(
        val details: VacancyDetails
    ) : VacancyDetailsUIState()

    data class Error(
        val errors: Errors?
    ) : VacancyDetailsUIState()
}
