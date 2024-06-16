package ru.practicum.android.diploma.filter.data.converter

import ru.practicum.android.diploma.filter.data.dto.IndustryDto
import ru.practicum.android.diploma.filter.data.dto.RegionDto
import ru.practicum.android.diploma.filter.domain.models.Area
import ru.practicum.android.diploma.filter.domain.models.Industry
import ru.practicum.android.diploma.filter.domain.models.Region

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
}
