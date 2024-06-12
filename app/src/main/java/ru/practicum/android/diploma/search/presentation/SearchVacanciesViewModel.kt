package ru.practicum.android.diploma.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.models.VacanciesSearchRequest
import ru.practicum.android.diploma.search.domain.models.VacancyPreview
import ru.practicum.android.diploma.search.domain.models.SearchResult
import ru.practicum.android.diploma.search.ui.models.SearchUiEvent
import ru.practicum.android.diploma.search.ui.models.SearchUiState

class SearchVacanciesViewModel(
    private val searchInteractor: SearchInteractor
) : ViewModel() {

    init {
        // проверка работоспособности поиска и отображения результата
        search()
    }

    private var nextPage = 0
    private var totalVacansiesList: MutableList<VacancyPreview> = mutableListOf()
    private var isNextPageLoading: Boolean = false

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Default)
    val uiState = _uiState.asStateFlow()

    fun onUiEvent(event: SearchUiEvent) {
        when (event) {
            SearchUiEvent.ClearText -> _uiState.value = SearchUiState.Default
        }
    }

    // тестовый метод поиска
    private fun search() {
        viewModelScope.launch {
            _uiState.value = SearchUiState.Loading
            val result = searchInteractor.searchVacancies(VacanciesSearchRequest(nextPage, "заполярье сочи"))
            isNextPageLoading = true
            _uiState.value = when (result) {
                is SearchResult.Error -> SearchUiState.Error(result.error)
                is SearchResult.SearchContent -> if (result.count == 0) {
                    SearchUiState.EmptyResult
                } else {
                    SearchUiState.SearchResult(
                        addVacanciesToList(result.vacancies),
                        convert(result.count),
                        result.page,
                        result.pages
                    )
                }

                SearchResult.EmptyResult -> TODO()
            }
        }
    }

    // тестовый метод
    private fun convert(count: Int): String {
        return "Найдено $count вакансий"
    }

    private fun addVacanciesToList(newPartVacancies: List<VacancyPreview>): MutableList<VacancyPreview> {
        totalVacansiesList += newPartVacancies
        return totalVacansiesList
    }

    fun onLastItemReached(
        currentPage: Int,
        maxPage: Int
    ) {
        viewModelScope.launch {
            if (currentPage < maxPage && isNextPageLoading) {
                nextPage = currentPage + 1
                search()
                isNextPageLoading = false
            } else {
                _uiState.value = SearchUiState.FullLoaded
            }
        }
    }
}
