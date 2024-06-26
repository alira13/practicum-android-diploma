package ru.practicum.android.diploma.filter.data.api

import ru.practicum.android.diploma.filter.data.dto.SettingsDto

interface SettingsHandler {
    fun read(settingsKey: String): SettingsDto
    fun write(settingsDto: SettingsDto, settingsKey: String): Boolean
    fun clear()
}
