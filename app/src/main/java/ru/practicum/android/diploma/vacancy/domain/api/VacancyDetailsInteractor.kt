package ru.practicum.android.diploma.vacancy.domain.api

import ru.practicum.android.diploma.search.domain.models.Errors
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetailsR
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetailsRequest

interface VacancyDetailsInteractor {
    suspend fun getVacancyDetails(request: VacancyDetailsRequest): Pair<VacancyDetailsR?, Errors?>
}
