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
    override fun read(settingsKey: String): Settings {
        return converter.mapSettings(settingsHandler.read(settingsKey))
    }

    override fun write(writeRequest: WriteRequest, settingsKey: String): Boolean {
        return when (writeRequest) {
            is WriteRequest.WriteIndustry -> {
                val settingsToWrite = writeIndustry(writeRequest, settingsKey)
                settingsHandler.write(settingsToWrite, settingsKey)
            }

            is WriteRequest.WriteCountry -> {
                val settingsToWrite = writeCountry(writeRequest, settingsKey)
                settingsHandler.write(settingsToWrite, settingsKey)
            }

            is WriteRequest.WriteSalary -> {
                val settingsToWrite = writeSalary(writeRequest, settingsKey)
                settingsHandler.write(settingsToWrite, settingsKey)
            }

            is WriteRequest.WriteArea -> {
                val settingsToWrite = writeArea(writeRequest, settingsKey)
                settingsHandler.write(settingsToWrite, settingsKey)
            }

            is WriteRequest.WriteOnlyWithSalary -> {
                val settingsToWrite = writeOnlyWithSalary(writeRequest, settingsKey)
                settingsHandler.write(settingsToWrite, settingsKey)
            }

            is WriteRequest.WriteFilterOn -> {
                val settingsToWrite = writeFilterOn(writeRequest, settingsKey)
                settingsHandler.write(settingsToWrite, settingsKey)
            }

            is WriteRequest.WriteSettings -> {
                settingsHandler.write(converter.mapSettings(writeRequest.settings), settingsKey)
            }
        }
    }

    private fun writeIndustry(writeRequest: WriteRequest.WriteIndustry, settingsKey: String): SettingsDto {
        val settingsDto = settingsHandler.read(settingsKey)
        return SettingsDto(
            industry = converter.mapIndustry(writeRequest.industry),
            country = settingsDto.country,
            area = settingsDto.area,
            salary = settingsDto.salary,
            onlyWithSalary = settingsDto.onlyWithSalary,
            filterOn = settingsDto.filterOn,
        )
    }

    private fun writeCountry(writeRequest: WriteRequest.WriteCountry, settingsKey: String): SettingsDto {
        val settingsDto = settingsHandler.read(settingsKey)
        return SettingsDto(
            industry = settingsDto.industry,
            country = converter.mapCountry(writeRequest.country),
            salary = settingsDto.salary,
            area = settingsDto.area,
            onlyWithSalary = settingsDto.onlyWithSalary,
            filterOn = settingsDto.filterOn,
        )
    }

    private fun writeSalary(writeRequest: WriteRequest.WriteSalary, settingsKey: String): SettingsDto {
        val settingsDto = settingsHandler.read(settingsKey)
        return SettingsDto(
            industry = settingsDto.industry,
            country = settingsDto.country,
            salary = writeRequest.salary,
            area = settingsDto.area,
            onlyWithSalary = settingsDto.onlyWithSalary,
            filterOn = settingsDto.filterOn,
        )
    }

    private fun writeArea(writeRequest: WriteRequest.WriteArea, settingsKey: String): SettingsDto {
        val settingsDto = settingsHandler.read(settingsKey)
        return SettingsDto(
            industry = settingsDto.industry,
            country = settingsDto.country,
            salary = settingsDto.salary,
            area = converter.mapArea(writeRequest.area),
            onlyWithSalary = settingsDto.onlyWithSalary,
            filterOn = settingsDto.filterOn,
        )
    }

    private fun writeOnlyWithSalary(writeRequest: WriteRequest.WriteOnlyWithSalary, settingsKey: String): SettingsDto {
        val settingsDto = settingsHandler.read(settingsKey)
        return SettingsDto(
            industry = settingsDto.industry,
            country = settingsDto.country,
            salary = settingsDto.salary,
            area = settingsDto.area,
            onlyWithSalary = writeRequest.onlyWithSalary,
            filterOn = settingsDto.filterOn,
        )
    }

    private fun writeFilterOn(writeRequest: WriteRequest.WriteFilterOn, settingsKey: String): SettingsDto {
        val settingsDto = settingsHandler.read(settingsKey)
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
