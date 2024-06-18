package ru.practicum.android.diploma.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.models.SearchResult
import ru.practicum.android.diploma.search.domain.models.VacanciesSearchRequest
import ru.practicum.android.diploma.search.domain.models.VacancyPreview
import ru.practicum.android.diploma.search.ui.models.SearchUiEvent
import ru.practicum.android.diploma.search.ui.models.SearchUiState

class SearchVacanciesViewModel(
    private val searchInteractor: SearchInteractor
) : ViewModel() {

    private var pageToRequest = 0
    private var totalVacansiesList: MutableList<VacancyPreview> = mutableListOf()
    private var searchJob: Job? = null
    private var pagingJob: Job? = null

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Default())
    val uiState = _uiState.asStateFlow()

    private var lastSearchRequest: String? = null

    fun onUiEvent(event: SearchUiEvent) {
        when (event) {
            SearchUiEvent.ClearText -> onRequestCleared()
            is SearchUiEvent.QueryInput -> onQueryInput(event.expression)
            is SearchUiEvent.LastItemReached -> onLastItemReached()
        }
    }

    private fun onRequestCleared() {
        searchJob?.cancel()
        _uiState.value = SearchUiState.Default()
    }

    private fun onQueryInput(expression: String) {
        if (
            expression.isEmpty()
            || expression == "null"
        ) {
            onRequestCleared()
        } else if (expression != lastSearchRequest) {
            _uiState.value = SearchUiState.EditingRequest
            resetSearchParams(expression)
            searchJob?.cancel()
            search(lastSearchRequest!!, true)
        }
    }

    private fun resetSearchParams(request: String) {
        lastSearchRequest = request
        pageToRequest = 0
        totalVacansiesList = mutableListOf()
    }

    private fun search(
        searchRequest: String,
        withDelay: Boolean
    ) {
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            if (withDelay) {
                delay(SEARCH_DEBOUNCE_DELAY_MILLIS)
            }
            if (pageToRequest == 0) {
                _uiState.value = SearchUiState.Loading()
            }
            val result = searchInteractor.searchVacancies(VacanciesSearchRequest(pageToRequest, searchRequest))
            _uiState.value = convertResult(result)
        }
    }

    private fun convertResult(result: SearchResult): SearchUiState {
        return when (result) {
            is SearchResult.Error -> if (pageToRequest == 0) {
                SearchUiState.FirstRequestError(error = result.error)
            } else {
                SearchUiState.PagingError(error = result.error)
            }

            is SearchResult.SearchContent -> if (isEmpty(result.vacancies)) {
                SearchUiState.EmptyResult()
            } else {
                SearchUiState.SearchResult(
                    vacancies = addVacanciesToList(result.vacancies),
                    count = result.count,
                    isItFirstPage = pageToRequest == 0,
                    isFullLoaded = result.page == result.pages
                )
            }
        }
    }

    private fun isEmpty(vacancies: List<VacancyPreview>): Boolean {
        val condition1 = pageToRequest == 0 && vacancies.isEmpty()
        val condition2 = pageToRequest != 0 && totalVacansiesList.isEmpty()
        return condition1 || condition2
    }

    private fun addVacanciesToList(newPartVacancies: List<VacancyPreview>): MutableList<VacancyPreview> {
        totalVacansiesList += newPartVacancies
        return totalVacansiesList
    }

    private fun onLastItemReached() {
        _uiState.value = SearchUiState.PagingLoading()
        pagingJob?.cancel()
        pagingJob = viewModelScope.launch(Dispatchers.IO) {
            delay(PAGING_DEBOUNCE_DELAY_MILLIS)
            pageToRequest += 1
            search(lastSearchRequest!!, false)
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
        private const val PAGING_DEBOUNCE_DELAY_MILLIS = 2000L
    }
}
