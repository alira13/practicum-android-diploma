package ru.practicum.android.diploma.filter.data.dto

import ru.practicum.android.diploma.search.data.dto.reponse.Response

data class IndustriesResponse(
    val industries: List<IndustryDto>
) : Response()
