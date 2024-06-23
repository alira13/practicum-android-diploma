package ru.practicum.android.diploma.filter.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.filter.domain.api.FilterInteractor
import ru.practicum.android.diploma.filter.domain.api.SettingsInteractor
import ru.practicum.android.diploma.filter.domain.models.FilterResult
import ru.practicum.android.diploma.filter.domain.models.Industry
import ru.practicum.android.diploma.filter.domain.models.WriteRequest
import ru.practicum.android.diploma.filter.ui.model.FilterUIState

class FilterIndustryViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val filterInteractor: FilterInteractor
) : ViewModel() {

    var industry: Industry = Industry("", "")
    private val uiState = MutableLiveData<FilterUIState>()
    private val writeCompleteState = MutableLiveData<Boolean>()
    fun getUiState(): LiveData<FilterUIState> = uiState

    fun getWriteComplete(): LiveData<Boolean> = writeCompleteState

    var industries = mutableListOf<Industry>()
    init {
        viewModelScope.launch {
            uiState.value = FilterUIState.Loading
            val result = filterInteractor.getIndustries()
            processResult(result)
        }
    }

    private fun processResult(result: FilterResult) {
        when (result) {
            is FilterResult.Industries -> {
                uiState.value = FilterUIState.Content(result.industries)
                industries = result.industries.toMutableList()
            }

            is FilterResult.Error -> {
                uiState.value = FilterUIState.Error(result.error)
            }

            else ->{}
        }
    }

    fun filterIndustries(filter: String) {
        viewModelScope.launch {
            val filteredList = withContext(Dispatchers.Default) {
                industries.filter { it.name.contains(filter, true) }
            }
            if (filteredList.isNotEmpty()) {
                uiState.value = FilterUIState.Content(filteredList)
            } else {
                uiState.value = FilterUIState.Empty
            }
        }
    }

    fun writeIndustry() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                settingsInteractor.write(WriteRequest.WriteIndustry(industry))
            }
            writeCompleteState.value = result
        }
    }
}
