package ru.practicum.android.diploma.filter.data.api

import ru.practicum.android.diploma.filter.data.dto.SettingsDto

interface SettingsHandler {
    fun read(): SettingsDto?
    fun write(settingsDto: SettingsDto): Boolean
    fun clear()
}
