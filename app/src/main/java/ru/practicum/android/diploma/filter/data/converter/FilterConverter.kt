package ru.practicum.android.diploma.filter.data.converter

import ru.practicum.android.diploma.filter.data.dto.CountryDto
import ru.practicum.android.diploma.filter.data.dto.IndustryDto
import ru.practicum.android.diploma.filter.data.dto.RegionDto
import ru.practicum.android.diploma.filter.data.dto.SettingsDto
import ru.practicum.android.diploma.filter.domain.models.Area
import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.filter.domain.models.Industry
import ru.practicum.android.diploma.filter.domain.models.Region
import ru.practicum.android.diploma.filter.domain.models.Settings
import ru.practicum.android.diploma.search.data.dto.reponse.AreaDto

class FilterConverter {
    fun mapIndustryDto(dto: IndustryDto): Industry {
        return Industry(
            id = dto.id,
            name = dto.name
        )
    }

    fun mapRegionDto(dto: RegionDto): Region {
        return Region(
            areas = dto.areas.map { areaDto ->
                Area(
                    id = areaDto.id,
                    name = areaDto.name,
                    parentId = dto.id
                )
            },
            id = dto.id,
            name = dto.name
        )
    }

    fun mapIndustry(industry: Industry): IndustryDto {
        return IndustryDto(
            id = industry.id,
            name = industry.name
        )
    }

    fun mapSettingsDto(dto: SettingsDto): Settings {
        return Settings(
            industry = Industry(
                id = dto.industry.id,
                name = dto.industry.name
            ),
            country = Country(
                id = dto.country.id,
                name = dto.country.name
            ),
            area = Area(
                id = dto.area.id,
                name = dto.area.name,
                parentId = dto.country.id
            ),
            salary = dto.salary,
            onlyWithSalary = dto.onlyWithSalary,
            filterOn = dto.filterOn,
        )
    }

    fun mapCountry(country: Country): CountryDto {
        return CountryDto(
            id = country.id,
            name = country.name
        )
    }

    fun mapArea(area: Area): AreaDto {
        return AreaDto(
            id = area.id,
            name = area.name,
            parentId = area.parentId
        )
    }

    fun mapSettings(settings: Settings): SettingsDto {
        return SettingsDto(
            industry = IndustryDto(
                id = settings.industry.id,
                name = settings.industry.name
            ),
            country = CountryDto(
                id = settings.country.id,
                name = settings.country.name
            ),
            area = AreaDto(
                id = settings.area.id,
                name = settings.area.name,
                parentId = settings.country.id
            ),
            salary = settings.salary,
            onlyWithSalary = settings.onlyWithSalary,
            filterOn = settings.filterOn,
        )
    }
}
