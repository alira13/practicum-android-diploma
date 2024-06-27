package ru.practicum.android.diploma.search.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filter.domain.api.SettingsInteractor
import ru.practicum.android.diploma.filter.domain.models.Settings
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.models.SearchResult
import ru.practicum.android.diploma.search.domain.models.VacanciesSearchRequest
import ru.practicum.android.diploma.search.domain.models.VacancyPreview
import ru.practicum.android.diploma.search.ui.models.SearchUiEvent
import ru.practicum.android.diploma.search.ui.models.SearchUiState

class SearchVacanciesViewModel(
    private val searchInteractor: SearchInteractor,
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    private var pageToRequest = 0
    private var totalVacanciesList: MutableList<VacancyPreview> = mutableListOf()
    private var searchJob: Job? = null
    private var isNextPageLoading: Boolean = false
    private var isFullLoaded: Boolean = false
    private var count: String? = null
    private var lastFilterSettings: Settings? = null
    private var lastSearchRequest = DEFAULT_STRING_VALUE

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Default())
    val uiState = _uiState.asStateFlow()

    private val _filterOnState = MutableStateFlow(false)
    val filterOnState = _filterOnState.asStateFlow()

    fun onUiEvent(event: SearchUiEvent) {
        when (event) {
            SearchUiEvent.ClearText -> onRequestCleared()
            is SearchUiEvent.QueryInput -> onQueryInput(event.expression)
            is SearchUiEvent.LastItemReached -> onLastItemReached()
            SearchUiEvent.ResumeData -> resumeData()
            SearchUiEvent.OnFragmentResume -> onFragmentResume()
        }
    }

    private fun resumeData() {
        _uiState.value = SearchUiState.SearchResult(
            vacancies = totalVacanciesList,
            count = count!!,
            isItFirstPage = pageToRequest == 0,
            isFullLoaded = isFullLoaded
        )
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
            search(true)
        }
    }

    private fun resetSearchParams(request: String) {
        lastSearchRequest = request
        pageToRequest = 0
        totalVacanciesList = mutableListOf()
        isFullLoaded = false
        count = null
    }

    private fun search(
        withDelay: Boolean
    ) {
        searchJob = viewModelScope.launch {
            if (withDelay) {
                delay(SEARCH_DEBOUNCE_DELAY_MILLIS)
            }
            if (pageToRequest == 0) {
                _uiState.value = SearchUiState.Loading()
            }
            val result = searchInteractor.searchVacancies(
                VacanciesSearchRequest(
                    page = pageToRequest,
                    searchString = lastSearchRequest,
                    filterSettings = lastFilterSettings!!
                )
            )
            isNextPageLoading = true
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
                isFullLoaded = result.page == result.pages
                count = result.count
                SearchUiState.SearchResult(
                    vacancies = addVacanciesToList(result.vacancies),
                    count = count!!,
                    isItFirstPage = pageToRequest == 0,
                    isFullLoaded = isFullLoaded
                )
            }
        }
    }

    private fun isEmpty(vacancies: List<VacancyPreview>): Boolean {
        val condition1 = pageToRequest == 0 && vacancies.isEmpty()
        val condition2 = pageToRequest != 0 && totalVacanciesList.isEmpty()
        return condition1 || condition2
    }

    private fun addVacanciesToList(newPartVacancies: List<VacancyPreview>): MutableList<VacancyPreview> {
        totalVacanciesList += newPartVacancies
        return totalVacanciesList
    }

    private fun onLastItemReached() {
        _uiState.value = SearchUiState.PagingLoading()
        viewModelScope.launch(Dispatchers.IO) {
            if (isNextPageLoading) {
                pageToRequest += 1
                search(false)
                isNextPageLoading = false
            }
        }
    }

    private fun onFragmentResume() {
        Log.i("alex", "${settingsInteractor.read(OLD_SETTINGS_KEY)}")
        Log.i("alex", "${settingsInteractor.read(NEW_SETTINGS_KEY)}")
        val filterUpdated = updateFilterSettings()
        if (filterUpdated && lastSearchRequest.isNotEmpty()) {
            resetSearchParams(lastSearchRequest)
            search(false)
        }
    }

    private fun updateFilterSettings(): Boolean {
        val newSettings = readSettings()
        val condition = newSettings != lastFilterSettings
        lastFilterSettings = newSettings
        return condition
    }

    private fun readSettings(): Settings {
        val filterSettings = settingsInteractor.read(NEW_SETTINGS_KEY)
        _filterOnState.value = filterSettings.filterOn
        return filterSettings
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
        private const val DEFAULT_STRING_VALUE = ""
        const val OLD_SETTINGS_KEY = "old settings"
        const val NEW_SETTINGS_KEY = "new settings"
    }
}
