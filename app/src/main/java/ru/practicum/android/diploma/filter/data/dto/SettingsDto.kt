package ru.practicum.android.diploma.filter.data.dto

import ru.practicum.android.diploma.search.data.dto.reponse.AreaDto

data class SettingsDto(
    val industry: IndustryDto,
    val country: CountryDto,
    val area: AreaDto,
    val salary: Long,
    val onlyWithSalary: Boolean,
    val filterOn: Boolean,
    val isRequest: Boolean
)
