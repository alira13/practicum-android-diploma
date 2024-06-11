package ru.practicum.android.diploma.search.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.ui.models.SearchUiEvent
import ru.practicum.android.diploma.search.ui.models.SearchUiState

class SearchVacanciesViewModel(
    private val searchInteractor: SearchInteractor
): ViewModel() {

private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Default)
    val uiState = _uiState.asStateFlow()

    fun onUiEvent(event: SearchUiEvent){
        when(event){
            SearchUiEvent.ClearText -> _uiState.value = SearchUiState.Default
        }
    }


}
