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
    fun map(dto: IndustryDto): Industry {
        return Industry(
            id = dto.id,
            name = dto.name
        )
    }

    fun map(dto: RegionDto): Region {
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

    fun map(industry: Industry): IndustryDto {
        return IndustryDto(
            id = industry.id,
            name = industry.name
        )
    }

    fun map(region: Region): RegionDto {
        return RegionDto(
            areas = region.areas.map { area ->
                AreaDto(
                    id = area.id,
                    name = area.name
                )
            },
            id = region.id,
            name = region.name
        )
    }

    fun map(dto: SettingsDto): Settings {
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

    fun map(dto: CountryDto): Country {
        return Country(
            id = dto.id,
            name = dto.name
        )
    }

    fun map(dto: AreaDto): Area {
        return Area(
            id = dto.id,
            name = dto.name
        )
    }

    fun map(country: Country): CountryDto {
        return CountryDto(
            id = country.id,
            name = country.name
        )
    }

    fun map(area: Area): AreaDto {
        return AreaDto(
            id = area.id,
            name = area.name
        )
    }
}
