package ru.practicum.android.diploma.vacancy.domain.api

import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetailsR
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetailsRequest

interface VacancyDetailsRepository {
    suspend fun getVacancyDetails(request: VacancyDetailsRequest): Resource<VacancyDetailsR>
}
