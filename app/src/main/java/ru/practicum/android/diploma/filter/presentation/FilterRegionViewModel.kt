package ru.practicum.android.diploma.filter.presentation

import android.util.Log
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
import ru.practicum.android.diploma.filter.domain.models.FilterResult
import ru.practicum.android.diploma.filter.domain.models.Region
import ru.practicum.android.diploma.filter.ui.region.models.AreaUiState
import ru.practicum.android.diploma.filter.ui.region.models.RegionUiEvent

class FilterRegionViewModel(
    private val interactor: FilterInteractor,
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    private var totalRegionList: MutableList<Region> = mutableListOf()
    private var searchJob: Job? = null

    private val _uiState = MutableStateFlow<AreaUiState>(AreaUiState.Default())
    val uiState = _uiState.asStateFlow()

    private var lastSearchRequest: String? = null

    private var currentRegions: List<Region>? = null
    private var currentAreas: List<Area>? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val result = interactor.getRegions()
            _uiState.value = convertResult(result)
        }
    }

    private fun applySettings(regions: List<Region>) {
        val settings = settingsInteractor.read()
        when {
            settings.area != null -> getAreasByRegionId(regions, settings.area.id)
            else -> getAllAreas(regions)
        }
    }

    private fun getAllAreas(regions: List<Region>) {
        Log.d("MY", ">>>>>>>>>>>>getAllRegions")
        currentRegions = regions
        Log.d("MY", ">>>>>>>>>>>>getAllRegions ${regions}")
        currentAreas = currentRegions!!.map { it -> it.areas }.flatten()
        Log.d("MY", ">>>>>>>>>>>>getAllRegions ${currentAreas}")
    }

    private fun getAreasByRegionId(regions: List<Region>, regionId: String) {
        currentRegions = regions.filter { regionId -> regionId == regionId }
        currentAreas = currentRegions!!.first().areas
    }

    private fun getAreasByName(regions: List<Region>, areaName: String): List<Area> {
        return currentAreas!!.filter { area -> area.name == areaName }
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
            content = currentAreas!!
        )
    }

    private fun onRequestCleared() {
        searchJob?.cancel()
        _uiState.value = AreaUiState.Default()
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
            search(lastSearchRequest!!, true)
        }
    }

    private fun resetSearchParams(request: String) {
        lastSearchRequest = request
        totalRegionList = mutableListOf()
    }

    private fun search(
        searchRequest: String,
        withDelay: Boolean
    ) {
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            if (withDelay) {
                delay(SEARCH_DEBOUNCE_DELAY_MILLIS)
            }

            _uiState.value = AreaUiState.Loading()
            val result = getAreasByName(currentRegions!!, searchRequest)

            //_uiState.value = convertResult(result)
        }
    }


    private fun convertResult(result: FilterResult): AreaUiState {
        return when (result) {
            is FilterResult.Error -> {
                Log.d("MY", "error = ${result.error}")
                AreaUiState.Error(error = result.error)
            }

            is FilterResult.Regions -> {
                applySettings(result.regions)
                if (currentAreas.isNullOrEmpty()) {
                    Log.d("MY", "regions isEmpty = ${result.regions}")
                    AreaUiState.EmptyResult()
                } else {
                    Log.d("MY", "VM regions = ${result.regions}")
                    applySettings(result.regions)
                    Log.d("MY", "VM currentAreas = ${currentAreas}")
                    AreaUiState.SearchResult(true, currentAreas!!)
                }
            }

            //TODO Вот это очень странно
            is FilterResult.Industries -> {
                AreaUiState.EmptyResult()
            }
        }
    }

    fun saveRegion(regionId: String) {

    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
    }
}
