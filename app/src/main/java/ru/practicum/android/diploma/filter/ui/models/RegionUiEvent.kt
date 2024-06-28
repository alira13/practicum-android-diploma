package ru.practicum.android.diploma.filter.ui.models

interface RegionUiEvent {
    data object ClearText : RegionUiEvent
    data class QueryInput(val expression: String) : RegionUiEvent
    data object ResumeData : RegionUiEvent
}
