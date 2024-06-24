package ru.practicum.android.diploma.search.domain.impl

import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.api.SearchRepository
import ru.practicum.android.diploma.search.domain.models.SearchResult
import ru.practicum.android.diploma.search.domain.models.VacanciesSearchRequest

class SearchInteractorImpl(
    private val searchRepository: SearchRepository
) : SearchInteractor {
    override suspend fun searchVacancies(request: VacanciesSearchRequest): SearchResult {
        return searchRepository.searchVacancies(request)
    }
}
