package ru.practicum.android.diploma.filter.ui.region.models

interface RegionUiEvent {
    data object ClearText : RegionUiEvent
    data class QueryInput(val expression: String) : RegionUiEvent
    data object LastItemReached : RegionUiEvent
    data object ResumeData : RegionUiEvent
}
