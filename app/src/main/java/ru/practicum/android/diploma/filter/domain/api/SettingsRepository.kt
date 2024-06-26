package ru.practicum.android.diploma.filter.domain.api

import ru.practicum.android.diploma.filter.domain.models.Settings
import ru.practicum.android.diploma.filter.domain.models.WriteRequest

interface SettingsRepository {
    fun read(settingsKey: String): Settings
    fun write(writeRequest: WriteRequest, settingsKey: String): Boolean
    fun clear()
}
