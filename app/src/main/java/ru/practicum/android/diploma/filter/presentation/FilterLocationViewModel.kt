package ru.practicum.android.diploma.filter.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filter.domain.api.SettingsInteractor
import ru.practicum.android.diploma.filter.ui.models.FilterItem
import ru.practicum.android.diploma.filter.ui.models.FilterLocationUiState

class FilterLocationViewModel(
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        FilterLocationUiState(
            item1 = FilterItem.Absent,
            item2 = FilterItem.Absent,
            approveButtonVisibility = false
        )
    )
    val uiState = _uiState.asStateFlow()

    fun updateFilters() {
        viewModelScope.launch(Dispatchers.IO) {
            val settings = settingsInteractor.read()
            val countryName = settings.country.name
            val item1 = if (countryName.isEmpty()) {
                FilterItem.Absent
            } else {
                FilterItem.Present(itemName = countryName)
            }
            val regionName = settings.area.name
            val item2 = if (regionName.isEmpty()) {
                FilterItem.Absent
            } else {
                FilterItem.Present(itemName = regionName)
            }
            val buttonVisibility = !(countryName.isEmpty() && regionName.isEmpty())
            _uiState.value = FilterLocationUiState(item1, item2, buttonVisibility)
        }
    }
}
