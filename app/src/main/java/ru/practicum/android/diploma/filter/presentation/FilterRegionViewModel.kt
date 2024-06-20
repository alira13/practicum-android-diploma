package ru.practicum.android.diploma.filter.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filter.domain.api.FilterInteractor
import ru.practicum.android.diploma.filter.domain.models.FilterResult
import ru.practicum.android.diploma.filter.domain.models.Region
import ru.practicum.android.diploma.filter.ui.region.models.RegionUiEvent
import ru.practicum.android.diploma.filter.ui.region.models.RegionUiState

class FilterRegionViewModel(
    private val interactor: FilterInteractor
) : ViewModel() {

    private var pageToRequest = 0
    private var totalVacanciesList: MutableList<Region> = mutableListOf()
    private var searchJob: Job? = null
    private var isNextPageLoading: Boolean = false
    private var isFullLoaded: Boolean = false
    private var count: String? = null

    private val _uiState = MutableStateFlow<RegionUiState>(RegionUiState.Default())
    val uiState = _uiState.asStateFlow()

    private var lastSearchRequest: String? = null

    fun saveRegion(regionId: String){

    }
    fun onUiEvent(event: RegionUiEvent) {
        when (event) {
            RegionUiEvent.ClearText -> onRequestCleared()
            is RegionUiEvent.QueryInput -> onQueryInput(event.expression)
            is RegionUiEvent.LastItemReached -> onLastItemReached()
            RegionUiEvent.ResumeData -> resumeData()
        }
    }

    private fun resumeData() {
        _uiState.value = RegionUiState.SearchResult(
            content = totalVacanciesList,
            count = count!!,
            isItFirstPage = pageToRequest == 0,
            isFullLoaded = isFullLoaded
        )
    }

    private fun onRequestCleared() {
        searchJob?.cancel()
        _uiState.value = RegionUiState.Default()
    }

    private fun onQueryInput(expression: String) {
        if (
            expression.isEmpty()
            || expression == "null"
        ) {
            onRequestCleared()
        } else if (expression != lastSearchRequest) {
            _uiState.value = RegionUiState.EditingRequest
            resetSearchParams(expression)
            searchJob?.cancel()
            search(lastSearchRequest!!, true)
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
        searchRequest: String,
        withDelay: Boolean
    ) {
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            if (withDelay) {
                delay(SEARCH_DEBOUNCE_DELAY_MILLIS)
            }
            if (pageToRequest == 0) {
                _uiState.value = RegionUiState.Loading()
            }

            val result  = interactor.getRegions()
            //val result = interactor.getRegions(VacanciesSearchRequest(pageToRequest, searchRequest))
            isNextPageLoading = true
            _uiState.value = convertResult(result)
        }
    }

    private fun convertResult(result: FilterResult): RegionUiState {
        return when (result) {
            is FilterResult.Error -> if (pageToRequest == 0) {
                RegionUiState.FirstRequestError(error = result.error)
            } else {
                RegionUiState.PagingError(error = result.error)
            }

            is FilterResult.Regions -> if (isEmpty(result.regions)) {
                RegionUiState.EmptyResult()
            } else {
                //TODO Тут удалить вообще isFullLoaded
                isFullLoaded = true
                count = result.regions.count().toString()
                RegionUiState.SearchResult(
                    content = addVacanciesToList(result.regions),
                    count = count!!,
                    isItFirstPage = pageToRequest == 0,
                    isFullLoaded = isFullLoaded
                )
            }

            //TODO Вот это очень странно
            is FilterResult.Industries -> {
                RegionUiState.EmptyResult()
            }
        }
    }

    private fun isEmpty(vacancies: List<Region>): Boolean {
        val condition1 = pageToRequest == 0 && vacancies.isEmpty()
        val condition2 = pageToRequest != 0 && totalVacanciesList.isEmpty()
        return condition1 || condition2
    }

    private fun addVacanciesToList(newPartVacancies: List<Region>): MutableList<Region> {
        totalVacanciesList += newPartVacancies
        return totalVacanciesList
    }

    private fun onLastItemReached() {
        _uiState.value = RegionUiState.PagingLoading()
        viewModelScope.launch(Dispatchers.IO) {
            if (isNextPageLoading) {
                pageToRequest += 1
                search(lastSearchRequest!!, false)
                isNextPageLoading = false
            }
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
    }
}
