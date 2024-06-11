package ru.practicum.android.diploma.search.ui.models

interface SearchUiEvent {
    data class OnItemClick(val id: Long): SearchUiEvent
    object clearText: SearchUiEvent
}
