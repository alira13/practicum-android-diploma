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

    fun mapRegion(dto: RegionDto): Region {
        return Region(
            areas = dto.areas.map { areaDto ->
                Area(
                    id = areaDto.id,
                    name = dto.name
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

    fun mapSettings(dto: SettingsDto): Settings {
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
                name = dto.area.name
            ),
            salary = dto.salary,
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
            name = area.name
        )
    }
}
