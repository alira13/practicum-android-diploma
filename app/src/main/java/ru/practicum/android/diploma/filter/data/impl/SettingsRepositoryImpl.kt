package ru.practicum.android.diploma.filter.data.impl

import ru.practicum.android.diploma.filter.data.converter.FilterConverter
import ru.practicum.android.diploma.filter.data.dto.SettingsDto
import ru.practicum.android.diploma.filter.data.local.SettingsHandlerImpl
import ru.practicum.android.diploma.filter.domain.api.SettingsRepository
import ru.practicum.android.diploma.filter.domain.models.Settings
import ru.practicum.android.diploma.filter.domain.models.WriteRequest

class SettingsRepositoryImpl(
    private val converter: FilterConverter,
    private val settingsHandler: SettingsHandlerImpl,
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
                    country = settingsDto.country,
                    area = settingsDto.area,
                    salary = settingsDto.salary
                )
                settingsHandler.write(settingsToWrite)
            }

            is WriteRequest.WriteCountry -> {
                val settingsDto = settingsHandler.read()
                val settingsToWrite = SettingsDto(
                    industry = settingsDto.industry,
                    country = converter.map(writeRequest.country),
                    salary = settingsDto.salary,
                    area = settingsDto.area
                )
                settingsHandler.write(settingsToWrite)
            }

            is WriteRequest.WriteSalary -> {
                val settingsDto = settingsHandler.read()
                val settingsToWrite = SettingsDto(
                    industry = settingsDto.industry,
                    country = settingsDto.country,
                    salary = writeRequest.salary,
                    area = settingsDto.area
                )
                settingsHandler.write(settingsToWrite)
            }

            is WriteRequest.WriteArea -> {
                val settingsDto = settingsHandler.read()
                val settingsToWrite = SettingsDto(
                    industry = settingsDto.industry,
                    country = settingsDto.country,
                    salary = settingsDto.salary,
                    area = converter.map(writeRequest.area)
                )
                settingsHandler.write(settingsToWrite)
            }
        }
    }

    override fun clear() {
        settingsHandler.clear()
    }
}
