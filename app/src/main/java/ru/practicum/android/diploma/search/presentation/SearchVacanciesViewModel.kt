package ru.practicum.android.diploma.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.models.VacanciesSearchRequest
import ru.practicum.android.diploma.search.domain.models.VacancyPreview
import ru.practicum.android.diploma.search.ui.models.SearchUiEvent
import ru.practicum.android.diploma.search.ui.models.SearchUiState

class SearchVacanciesViewModel(
    private val searchInteractor: SearchInteractor
) : ViewModel() {

    private var currentPage = 0
    private var maxPage = 0
    private var vacansiesList: MutableList<VacancyPreview> = mutableListOf()
    private var isNextPageLoading: Boolean = true

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Default)
    val uiState = _uiState.asStateFlow()

    fun onUiEvent(event: SearchUiEvent) {
        when (event) {
            SearchUiEvent.ClearText -> _uiState.value = SearchUiState.Default
        }
    }

    fun onLastItemReached() {
        viewModelScope.launch {
            if ((currentPage < maxPage) && !isNextPageLoading) {
                val nextPage = currentPage + 1
                searchInteractor.searchVacancies(VacanciesSearchRequest(nextPage, "столяр"))
                isNextPageLoading = true
            } else {
                _uiState.value = SearchUiState.FullLoaded
            }
        }
    }
}
