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
import ru.practicum.android.diploma.filter.domain.api.SettingsInteractor
import ru.practicum.android.diploma.filter.domain.models.Area
import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.filter.domain.models.FilterResult
import ru.practicum.android.diploma.filter.domain.models.Region
import ru.practicum.android.diploma.filter.domain.models.Settings
import ru.practicum.android.diploma.filter.domain.models.WriteRequest
import ru.practicum.android.diploma.filter.ui.models.AreaUiState
import ru.practicum.android.diploma.filter.ui.models.RegionUiEvent

class FilterRegionViewModel(
    private val interactor: FilterInteractor,
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    private var totalRegionList: MutableList<Region> = mutableListOf()
    private var searchJob: Job? = null

    private var currentRegions: List<Region> = emptyList()
    private var currentAreas: List<Area> = emptyList()
    private var settings: Settings? = null

    private var lastSearchRequest: String? = null
    private var isFirstRequest = true

    private val _uiState = MutableStateFlow<AreaUiState>(AreaUiState.Default(content = currentAreas))
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val result = interactor.getRegions()
            _uiState.value = convertResult(result)
        }
    }

    private fun convertResult(result: FilterResult): AreaUiState {
        return when (result) {
            is FilterResult.Error -> {
                AreaUiState.Error(error = result.error)
            }

            is FilterResult.Regions -> {
                applySettings(result.regions)
                when {
                    currentAreas.isNotEmpty() && isFirstRequest -> {
                        isFirstRequest = false
                        AreaUiState.Default(true, content = currentAreas)
                    }

                    currentAreas.isEmpty() -> AreaUiState.EmptyResult()
                    else -> AreaUiState.SearchResult(true, currentAreas)
                }
            }

            is FilterResult.Industries -> {
                AreaUiState.EmptyResult()
            }
        }
    }

    private fun applySettings(regions: List<Region>) {
        settings = settingsInteractor.read(SETTINGS_KEY)
        getAllAreas(regions)
        when {
            settings == null -> getAllAreas(regions)
            settings!!.country.name.isNotEmpty() -> getAreasByRegionId(regions, settings!!.country.id)
            else -> getAllAreas(regions)
        }
    }

    private fun getAllAreas(regions: List<Region>) {
        currentRegions = regions
        currentAreas = currentRegions.map { it.areas }.flatten().sortedBy { it.name }
    }

    private fun getAreasByRegionId(regions: List<Region>, regionId: String) {
        currentRegions = regions.filter { region -> region.id == regionId }
        currentAreas = currentRegions.first().areas.sortedBy { it.name }
    }

    private fun getAreasByName(areaName: String): List<Area> {
        return currentAreas.filter { it.name.startsWith(areaName, true) }
    }

    fun onUiEvent(event: RegionUiEvent) {
        when (event) {
            RegionUiEvent.ClearText -> onRequestCleared()
            is RegionUiEvent.QueryInput -> onQueryInput(event.expression)
            RegionUiEvent.ResumeData -> resumeData()
        }
    }

    private fun resumeData() {
        _uiState.value = AreaUiState.SearchResult(
            content = currentAreas
        )
    }

    private fun onRequestCleared() {
        searchJob?.cancel()
        _uiState.value = AreaUiState.Default(content = currentAreas)
    }

    private fun onQueryInput(expression: String) {
        if (
            expression.isEmpty()
            || expression == "null"
        ) {
            onRequestCleared()
        } else if (expression != lastSearchRequest) {
            _uiState.value = AreaUiState.EditingRequest
            resetSearchParams(expression)
            searchJob?.cancel()
            search(lastSearchRequest!!)
        }
    }

    private fun resetSearchParams(request: String) {
        lastSearchRequest = request
        totalRegionList = mutableListOf()
    }

    private fun search(
        searchRequest: String
    ) {
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            delay(SEARCH_DEBOUNCE_DELAY_MILLIS)
            _uiState.value = AreaUiState.Loading()

            val foundAreas = getAreasByName(searchRequest)
            when {
                foundAreas.isEmpty() -> {
                    _uiState.value = AreaUiState.EmptyResult()
                }

                else -> {
                    _uiState.value = AreaUiState.SearchResult(true, foundAreas)
                }
            }
        }
    }

    fun saveSettings(area: Area) {
        val region = currentRegions.filter { region -> region.id == area.parentId }.first()
        settingsInteractor.write(WriteRequest.WriteCountry(Country(region.id, region.name)), SETTINGS_KEY)
        settingsInteractor.write(WriteRequest.WriteArea(area), SETTINGS_KEY)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
        const val SETTINGS_KEY = "settings key"
    }
}
