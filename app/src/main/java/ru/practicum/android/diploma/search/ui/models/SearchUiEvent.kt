package ru.practicum.android.diploma.search.ui.models

interface SearchUiEvent {
    data object ClearText : SearchUiEvent
    data class QueryInput(val s: CharSequence?) : SearchUiEvent
    data class LastItemReached(
        val currentPage: Int,
        val maxPage: Int
    ) : SearchUiEvent
}
