package ru.practicum.android.diploma.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.models.SearchResult
import ru.practicum.android.diploma.search.domain.models.VacanciesSearchRequest
import ru.practicum.android.diploma.search.domain.models.VacancyPreview
import ru.practicum.android.diploma.search.ui.models.SearchUiEvent
import ru.practicum.android.diploma.search.ui.models.SearchUiState
import ru.practicum.android.diploma.util.debounce

class SearchVacanciesViewModel(
    private val searchInteractor: SearchInteractor
) : ViewModel() {

    private var pageToRequest = 0
    private var currentPage = 0
    private var maxPages = 1
    private var totalVacansiesList: MutableList<VacancyPreview> = mutableListOf()
    private var isNextPageLoading: Boolean = false

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Default)
    val uiState = _uiState.asStateFlow()

    private var lastSearchRequest: String? = null
    private val searchDebounce = debounce<String>(
        delayMillis = SEARCH_DEBOUNCE_DELAY_MILLIS,
        coroutineScope = viewModelScope,
        useLastParam = true
    ) { searchRequest ->
        search(searchRequest)
    }

    fun onUiEvent(event: SearchUiEvent) {
        when (event) {
            SearchUiEvent.ClearText -> _uiState.value = SearchUiState.Default
            is SearchUiEvent.QueryInput -> onQueryInput(event.s)
            is SearchUiEvent.LastItemReached -> onLastItemReached()
        }
    }

    private fun onQueryInput(s: CharSequence?) {
        if (!s.isNullOrEmpty()) {
            _uiState.value = SearchUiState.EditingRequest
            resetSearchParams(s.toString())
            searchDebounce(lastSearchRequest!!)
        }
    }

    private fun resetSearchParams(request: String) {
        lastSearchRequest = request
        pageToRequest = 0
        currentPage = 0
        maxPages = 0
        totalVacansiesList = mutableListOf()
    }

    private fun search(searchRequest: String) {
        viewModelScope.launch {
            _uiState.value = SearchUiState.Loading(isItFirstPage = pageToRequest == 0)
            val result = searchInteractor.searchVacancies(VacanciesSearchRequest(pageToRequest, searchRequest))
            isNextPageLoading = true
            _uiState.value = when (result) {
                is SearchResult.Error -> SearchUiState.Error(
                    error = result.error,
                    isItFirstPage = pageToRequest == 0
                )

                is SearchResult.SearchContent -> if (result.count == 0) {
                    SearchUiState.EmptyResult
                } else {
                    currentPage = result.page
                    maxPages = result.pages
                    SearchUiState.SearchResult(
                        addVacanciesToList(result.vacancies),
                        result.count.toString()
                    )
                }
            }
        }
    }


    private fun addVacanciesToList(newPartVacancies: List<VacancyPreview>): MutableList<VacancyPreview> {
        totalVacansiesList += newPartVacancies
        return totalVacansiesList
    }

    private fun onLastItemReached() {
        viewModelScope.launch {
            if (pageToRequest < maxPages && isNextPageLoading) {
                pageToRequest += 1
                search(lastSearchRequest!!)
                isNextPageLoading = false
            } else {
                _uiState.value = SearchUiState.FullLoaded
            }
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
    }
}
