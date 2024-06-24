package ru.practicum.android.diploma.filter.ui.models

sealed interface FilterLocationUiEvent {
    data object UpdateData : FilterLocationUiEvent
    data object ClearCountry : FilterLocationUiEvent
    data object ClearRegion : FilterLocationUiEvent
    data object ExitWithoutSavingChanges : FilterLocationUiEvent
}
