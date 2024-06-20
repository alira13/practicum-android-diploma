package ru.practicum.android.diploma.filter.domain.api

import ru.practicum.android.diploma.filter.domain.models.Settings
import ru.practicum.android.diploma.filter.domain.models.WriteRequest

interface SettingsInteractor {
    fun read(): Settings
    fun write(writeRequest: WriteRequest): Boolean
    fun clear()
}
