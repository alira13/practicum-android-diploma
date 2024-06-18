package ru.practicum.android.diploma.filter.data.impl

import ru.practicum.android.diploma.filter.data.converter.FilterConverter
import ru.practicum.android.diploma.filter.data.local.SettingsHandler
import ru.practicum.android.diploma.filter.domain.api.SettingsRepository
import ru.practicum.android.diploma.filter.domain.models.Settings
import ru.practicum.android.diploma.filter.domain.models.WriteRequest

class SettingsRepositoryImpl(
    private val converter: FilterConverter,
    private val settingsHandler: SettingsHandler,
) : SettingsRepository {
    override fun read(): Settings {
        return converter.map(settingsHandler.read())
    }

    override fun write(writeRequest: WriteRequest): Boolean {
        return when (writeRequest) {
            is WriteRequest.WriteIndustry -> {
                val settingsDto = settingsHandler.read()
                settingsDto.industry = converter.map(writeRequest.industry)
                settingsHandler.write(settingsDto)
            }

            is WriteRequest.WriteRegion -> {
                val settingsDto = settingsHandler.read()
                settingsDto.region = converter.map(writeRequest.region)
                settingsHandler.write(settingsDto)
            }

            is WriteRequest.WriteSalary -> {
                val settingsDto = settingsHandler.read()
                settingsDto.salary = writeRequest.salary
                settingsHandler.write(settingsDto)
            }
        }
    }

    override fun clear() {
        settingsHandler.clear()
    }
}
