package ru.practicum.android.diploma.filter.domain.models

import ru.practicum.android.diploma.filter.data.dto.IndustryDto
import ru.practicum.android.diploma.filter.data.dto.RegionDto

data class Settings(
    val industry: IndustryDto?,
    val region: RegionDto?,
    val salary: Int?
)
