package ru.practicum.android.diploma.search.domain.api

import ru.practicum.android.diploma.search.domain.models.Request
import ru.practicum.android.diploma.search.domain.models.Vacancies
import ru.practicum.android.diploma.util.Resource

interface SearchRepository {
    suspend fun searchVacancies(request: Request): Resource<Vacancies>
}
