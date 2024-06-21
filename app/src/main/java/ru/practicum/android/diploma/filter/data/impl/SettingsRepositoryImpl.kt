package ru.practicum.android.diploma.filter.data.impl

import ru.practicum.android.diploma.filter.data.api.SettingsHandler
import ru.practicum.android.diploma.filter.data.converter.FilterConverter
import ru.practicum.android.diploma.filter.data.dto.SettingsDto
import ru.practicum.android.diploma.filter.domain.api.SettingsRepository
import ru.practicum.android.diploma.filter.domain.models.Settings
import ru.practicum.android.diploma.filter.domain.models.WriteRequest

class SettingsRepositoryImpl(
    private val converter: FilterConverter,
    private val settingsHandler: SettingsHandler,
) : SettingsRepository {
    override fun read(): Settings {
        return converter.mapSettings(settingsHandler.read())
    }

    override fun write(writeRequest: WriteRequest): Boolean {
        return when (writeRequest) {
            is WriteRequest.WriteIndustry -> {
                val settingsToWrite = writeIndustry(writeRequest)
                settingsHandler.write(settingsToWrite)
            }

            is WriteRequest.WriteCountry -> {
                val settingsToWrite = writeCountry(writeRequest)
                settingsHandler.write(settingsToWrite)
            }

            is WriteRequest.WriteSalary -> {
                val settingsToWrite = writeSalary(writeRequest)
                settingsHandler.write(settingsToWrite)
            }

            is WriteRequest.WriteArea -> {
                val settingsToWrite = writeArea(writeRequest)
                settingsHandler.write(settingsToWrite)
            }

            is WriteRequest.WriteOnlyWithSalary -> {
                val settingsToWrite = writeOnlyWithSalary(writeRequest)
                settingsHandler.write(settingsToWrite)
            }

            is WriteRequest.WriteFilterOn -> {
                val settingsToWrite = writeFilterOn(writeRequest)
                settingsHandler.write(settingsToWrite)
            }
        }
    }

    private fun writeIndustry(writeRequest: WriteRequest.WriteIndustry): SettingsDto {
        val settingsDto = settingsHandler.read()
        return SettingsDto(
            industry = converter.mapIndustry(writeRequest.industry),
            country = settingsDto.country,
            area = settingsDto.area,
            salary = settingsDto.salary,
            onlyWithSalary = settingsDto.onlyWithSalary,
            filterOn = settingsDto.filterOn,
        )
    }

    private fun writeCountry(writeRequest: WriteRequest.WriteCountry): SettingsDto {
        val settingsDto = settingsHandler.read()
        return SettingsDto(
            industry = settingsDto.industry,
            country = converter.mapCountry(writeRequest.country),
            salary = settingsDto.salary,
            area = settingsDto.area,
            onlyWithSalary = settingsDto.onlyWithSalary,
            filterOn = settingsDto.filterOn,
        )
    }

    private fun writeSalary(writeRequest: WriteRequest.WriteSalary): SettingsDto {
        val settingsDto = settingsHandler.read()
        return SettingsDto(
            industry = settingsDto.industry,
            country = settingsDto.country,
            salary = writeRequest.salary,
            area = settingsDto.area,
            onlyWithSalary = settingsDto.onlyWithSalary,
            filterOn = settingsDto.filterOn,
        )
    }

    private fun writeArea(writeRequest: WriteRequest.WriteArea): SettingsDto {
        val settingsDto = settingsHandler.read()
        return SettingsDto(
            industry = settingsDto.industry,
            country = settingsDto.country,
            salary = settingsDto.salary,
            area = converter.mapArea(writeRequest.area),
            onlyWithSalary = settingsDto.onlyWithSalary,
            filterOn = settingsDto.filterOn,
        )
    }

    private fun writeOnlyWithSalary(writeRequest: WriteRequest.WriteOnlyWithSalary): SettingsDto {
        val settingsDto = settingsHandler.read()
        return SettingsDto(
            industry = settingsDto.industry,
            country = settingsDto.country,
            salary = settingsDto.salary,
            area = settingsDto.area,
            onlyWithSalary = writeRequest.onlyWithSalary,
            filterOn = settingsDto.filterOn,
        )
    }

    private fun writeFilterOn(writeRequest: WriteRequest.WriteFilterOn): SettingsDto {
        val settingsDto = settingsHandler.read()
        return SettingsDto(
            industry = settingsDto.industry,
            country = settingsDto.country,
            salary = settingsDto.salary,
            area = settingsDto.area,
            onlyWithSalary = settingsDto.onlyWithSalary,
            filterOn = writeRequest.filterOn,
        )
    }

    override fun clear() {
        settingsHandler.clear()
    }
}
