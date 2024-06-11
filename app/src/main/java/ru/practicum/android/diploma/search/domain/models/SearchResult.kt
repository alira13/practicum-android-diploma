package ru.practicum.android.diploma.search.domain.models

sealed interface SearchResult {
    data class SearchContent(
        val vacancies: List<VacancyPreview>,
        val count: String
    ): SearchResult
    object EmptyResult: SearchResult
    data class Error(val error: Errors): SearchResult
}
