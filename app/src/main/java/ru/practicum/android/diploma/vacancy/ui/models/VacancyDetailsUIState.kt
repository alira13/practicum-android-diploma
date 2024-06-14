package ru.practicum.android.diploma.vacancy.ui.models

import ru.practicum.android.diploma.search.domain.models.Errors
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetailsR

sealed class VacancyDetailsUIState {
    data object Loading : VacancyDetailsUIState()
    data class Content(
        val details: VacancyDetailsR
    ) : VacancyDetailsUIState()

    data class Error(
        val errors: Errors?
    ) : VacancyDetailsUIState()
}
