package ru.practicum.android.diploma.search.data.dto.reponse

import ru.practicum.android.diploma.filter.data.dto.CountryDto

open class Response {
    var resultCode = 0
    var countriesList: List<CountryDto> = listOf()
}
