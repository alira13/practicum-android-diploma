package ru.practicum.android.diploma.vacancy.domain.api

import ru.practicum.android.diploma.search.domain.models.Errors
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetails
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetailsRequest

interface VacancyDetailsInteractor {
    suspend fun getVacancyDetails(request: VacancyDetailsRequest): Pair<VacancyDetails?, Errors?>
}
