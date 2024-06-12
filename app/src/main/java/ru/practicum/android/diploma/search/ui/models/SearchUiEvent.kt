package ru.practicum.android.diploma.search.ui.models

interface SearchUiEvent {
    data object ClearText : SearchUiEvent
    data class QueryInput(val s: CharSequence?) : SearchUiEvent
}
