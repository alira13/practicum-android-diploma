package ru.practicum.android.diploma.filter.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filter.domain.api.FilterInteractor
import ru.practicum.android.diploma.filter.domain.api.SettingsInteractor
import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.filter.domain.models.FilterCountryState
import ru.practicum.android.diploma.filter.domain.models.FilterResult
import ru.practicum.android.diploma.filter.domain.models.Region
import ru.practicum.android.diploma.filter.domain.models.WriteRequest

class FilterCountryViewModel(
    val filterInteractor: FilterInteractor,
    val settingsInteractor: SettingsInteractor
) : ViewModel() {

    private val _uiState = MutableStateFlow<FilterCountryState>(FilterCountryState.Default)
    val uiState = _uiState.asStateFlow()

    init {
        getCountyList()
    }

    fun chooseCountry(region: Region) {
        val request = WriteRequest.WriteCountry(
            Country(
                region.id,
                region.name
            )
        )
        settingsInteractor.write(request, SETTINGS_KEY)
    }

    private fun getCountyList() {
        viewModelScope.launch {
            val result = filterInteractor.getRegions()
            convertResult(result)
        }
    }

    private fun convertResult(result: FilterResult) {
        when (result) {
            is FilterResult.Regions -> _uiState.value = FilterCountryState.Content(result.regions)
            is FilterResult.Error -> _uiState.value = FilterCountryState.Error
            is FilterResult.Industries -> _uiState.value = FilterCountryState.Error
        }
    }

    companion object {
        const val SETTINGS_KEY = "settings key"
    }
}
