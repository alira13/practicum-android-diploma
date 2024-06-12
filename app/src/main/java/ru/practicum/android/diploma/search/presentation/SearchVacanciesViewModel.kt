package ru.practicum.android.diploma.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.models.SearchResult
import ru.practicum.android.diploma.search.domain.models.VacanciesSearchRequest
import ru.practicum.android.diploma.search.ui.models.SearchUiEvent
import ru.practicum.android.diploma.search.ui.models.SearchUiState
import ru.practicum.android.diploma.util.debounce

class SearchVacanciesViewModel(
    private val searchInteractor: SearchInteractor
) : ViewModel() {

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Default)
    val uiState = _uiState.asStateFlow()

    private val page = 1
    private val lastSearchRequest: String? = null
    private var searchJob: Job? = null
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
        }
    }

    private fun onQueryInput(s: CharSequence?){
        if(!s.isNullOrEmpty()){
            searchDebounce(s.toString())
        }
        else {
            _uiState.value = SearchUiState.Default
        }
    }

    // тестовый метод поиска
    private fun search(expression: String) {
        viewModelScope.launch {
            _uiState.value = SearchUiState.Loading
            val result = searchInteractor.searchVacancies(VacanciesSearchRequest(page, expression))
            _uiState.value = when (result) {
                is SearchResult.Error -> SearchUiState.Error(result.error)
                is SearchResult.SearchContent -> if (result.count == 0L) {
                    SearchUiState.EmptyResult
                } else {
                    SearchUiState.SearchResult(result.vacancies, convert(result.count))
                }
            }
        }
    }

    // тестовый метод
    private fun convert(count: Long): String {
        return "Найдено $count вакансий"
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
    }
}
