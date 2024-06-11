package ru.practicum.android.diploma.search.domain.impl

import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.api.SearchRepository
import ru.practicum.android.diploma.search.domain.models.Errors
import ru.practicum.android.diploma.search.domain.models.SearchResult
import ru.practicum.android.diploma.search.domain.models.Vacancies
import ru.practicum.android.diploma.search.domain.models.VacanciesSearchRequest
import ru.practicum.android.diploma.util.Resource

class SearchInteractorImpl(
    private val searchRepository: SearchRepository
) : SearchInteractor {
    override suspend fun searchVacancies(request: VacanciesSearchRequest): SearchResult {

        // временное изменение до подключение конвертера
        /*return when (val resource = searchRepository.searchVacancies(request)) {
            is Resource.Success -> Pair(resource.data, null)
            is Resource.Error -> Pair(null, resource.error)
        }*/
        return SearchResult.EmptyResult
    }
}
