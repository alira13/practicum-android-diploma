package ru.practicum.android.diploma.vacancy.ui.models

import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetails

sealed class VacancyDetailsUIState {
    data object Loading: VacancyDetailsUIState()
    data class Content(
        val details: VacancyDetails
    ): VacancyDetailsUIState()
    data class Error(
        val message: String
    ): VacancyDetailsUIState()
    data class Empty(
        val message: String
    ): VacancyDetailsUIState()
}
