package ru.practicum.android.diploma.filter.data.dto

import ru.practicum.android.diploma.search.data.dto.reponse.AreaDto

data class RegionDto(
    val areas: List<AreaDto>,
    val id: String,
    val name: String
)
