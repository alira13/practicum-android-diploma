package ru.practicum.android.diploma.vacancy.domain.api

import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetails
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetailsRequest

interface VacancyDetailsRepository {
    suspend fun getVacancyDetails(request: VacancyDetailsRequest): Resource<VacancyDetails>
}
