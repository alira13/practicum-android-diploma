package ru.practicum.android.diploma.search.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.models.VacanciesSearchRequest
import ru.practicum.android.diploma.search.domain.models.VacancyPreview
import ru.practicum.android.diploma.search.domain.models.SearchResult
import ru.practicum.android.diploma.search.ui.models.SearchUiEvent
import ru.practicum.android.diploma.search.ui.models.SearchUiState
import ru.practicum.android.diploma.util.debounce

class SearchVacanciesViewModel(
    private val searchInteractor: SearchInteractor
) : ViewModel() {


    private var nextPage = 0
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
            is SearchUiEvent.LastItemReached -> onLastItemReached(
                event.currentPage,
                event.maxPage
            )
        }
    }

    private fun onQueryInput(s: CharSequence?) {
        if (!s.isNullOrEmpty()) {
            _uiState.value = SearchUiState.EditingRequest
            lastSearchRequest = s.toString()
            searchDebounce(lastSearchRequest!!)
        }
    }

    private fun search(searchRequest: String) {
        Log.d("QQQ", "search ($lastSearchRequest)")
        viewModelScope.launch {
            _uiState.value = SearchUiState.Loading
            val result = searchInteractor.searchVacancies(VacanciesSearchRequest(nextPage, searchRequest))
            isNextPageLoading = true
            _uiState.value = when (result) {
                is SearchResult.Error -> SearchUiState.Error(result.error)
                is SearchResult.SearchContent -> if (result.count == 0) {
                    SearchUiState.EmptyResult
                } else {
                    SearchUiState.SearchResult(
                        addVacanciesToList(result.vacancies),
                        convert(result.count),
                        result.page,
                        result.pages
                    )
                }
            }
        }
    }

    // тестовый метод
    private fun convert(count: Int): String {
        return "Найдено $count вакансий"
    }

    private fun addVacanciesToList(newPartVacancies: List<VacancyPreview>): MutableList<VacancyPreview> {
        totalVacansiesList += newPartVacancies
        return totalVacansiesList
    }

    private fun onLastItemReached(
        currentPage: Int,
        maxPage: Int
    ) {
        viewModelScope.launch {
            if (currentPage < maxPage && isNextPageLoading) {
                nextPage = currentPage + 1
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
