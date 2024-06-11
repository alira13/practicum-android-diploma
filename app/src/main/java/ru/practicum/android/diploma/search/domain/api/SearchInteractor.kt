package ru.practicum.android.diploma.search.domain.api


import ru.practicum.android.diploma.search.domain.models.Errors
import ru.practicum.android.diploma.search.domain.models.SearchResult
import ru.practicum.android.diploma.search.domain.models.Vacancies
import ru.practicum.android.diploma.search.domain.models.SearchResult
import ru.practicum.android.diploma.search.domain.models.VacanciesSearchRequest

interface SearchInteractor {
    suspend fun searchVacancies(request: VacanciesSearchRequest): SearchResult
}
