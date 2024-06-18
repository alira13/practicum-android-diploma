package ru.practicum.android.diploma.filter.data.impl

import ru.practicum.android.diploma.filter.data.converter.FilterConverter
import ru.practicum.android.diploma.filter.data.dto.SettingsDto
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
                val settingsToWrite = SettingsDto(
                    industry = converter.map(writeRequest.industry),
                    region = settingsDto.region,
                    salary = settingsDto.salary
                )
                settingsHandler.write(settingsToWrite)
            }

            is WriteRequest.WriteRegion -> {
                val settingsDto = settingsHandler.read()
                val settingsToWrite = SettingsDto(
                    industry = settingsDto.industry,
                    region = converter.map(writeRequest.region),
                    salary = settingsDto.salary
                )
                settingsHandler.write(settingsToWrite)
            }

            is WriteRequest.WriteSalary -> {
                val settingsDto = settingsHandler.read()
                val settingsToWrite = SettingsDto(
                    industry = settingsDto.industry,
                    region = settingsDto.region,
                    salary = settingsDto.salary
                )
                settingsHandler.write(settingsToWrite)
            }
        }
    }

    override fun clear() {
        settingsHandler.clear()
    }
}
