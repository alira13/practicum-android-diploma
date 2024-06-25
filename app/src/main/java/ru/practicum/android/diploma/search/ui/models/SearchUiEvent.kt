package ru.practicum.android.diploma.search.ui.models

interface SearchUiEvent {
    data object ClearText : SearchUiEvent
    data class QueryInput(val expression: String) : SearchUiEvent
    data object LastItemReached : SearchUiEvent
    data object ResumeData : SearchUiEvent
}
