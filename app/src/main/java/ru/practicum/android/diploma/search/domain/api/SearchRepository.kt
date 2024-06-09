package ru.practicum.android.diploma.search.domain.api

import ru.practicum.android.diploma.search.domain.models.Vacancies
import ru.practicum.android.diploma.search.domain.models.VacanciesSearchRequest
import ru.practicum.android.diploma.util.Resource

interface SearchRepository {
    suspend fun searchVacancies(request: VacanciesSearchRequest): Resource<Vacancies>
}
