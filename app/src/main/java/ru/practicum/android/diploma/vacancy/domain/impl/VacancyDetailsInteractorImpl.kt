package ru.practicum.android.diploma.vacancy.domain.impl

import ru.practicum.android.diploma.search.domain.models.Errors
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailsInteractor
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailsRepository
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetailsR
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetailsRequest

class VacancyDetailsInteractorImpl(
    private val vacancyDetailsRepository: VacancyDetailsRepository
) : VacancyDetailsInteractor {
    override suspend fun getVacancyDetails(request: VacancyDetailsRequest): Pair<VacancyDetailsR?, Errors?> {
        return when (val resource = vacancyDetailsRepository.getVacancyDetails(request)) {
            is Resource.Success -> Pair(resource.data, null)
            is Resource.Error -> Pair(null, resource.error)
        }
    }
}
