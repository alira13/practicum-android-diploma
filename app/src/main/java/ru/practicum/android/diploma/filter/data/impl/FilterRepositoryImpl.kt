package ru.practicum.android.diploma.filter.data.impl

import ru.practicum.android.diploma.filter.data.converter.FilterConverter
import ru.practicum.android.diploma.filter.data.dto.IndustriesResponse
import ru.practicum.android.diploma.filter.data.dto.IndustryRequestDto
import ru.practicum.android.diploma.filter.data.dto.RegionsRequestDto
import ru.practicum.android.diploma.filter.data.dto.RegionsResponse
import ru.practicum.android.diploma.filter.domain.api.FilterRepository
import ru.practicum.android.diploma.filter.domain.models.FilterResult
import ru.practicum.android.diploma.search.data.CONNECTION_ERROR
import ru.practicum.android.diploma.search.data.INCORRECT_REQUEST
import ru.practicum.android.diploma.search.data.SUCCESS
import ru.practicum.android.diploma.search.data.api.NetworkClient
import ru.practicum.android.diploma.search.domain.models.Errors

class FilterRepositoryImpl(
    private val networkClient: NetworkClient,
    private val converter: FilterConverter
) : FilterRepository {
    override suspend fun getRegions(): FilterResult {
        val options = hashMapOf(
            Pair("locale", "RU"),
            Pair("host", "hh.ru")
        )
        val response = networkClient.doRequest(RegionsRequestDto(options))
        return when (response.resultCode) {
            SUCCESS -> {
                val resultList = (response as RegionsResponse).regions.map { converter.mapRegionDto(it) }
                FilterResult.Regions(resultList)
            }
            CONNECTION_ERROR -> {
                FilterResult.Error(Errors.ConnectionError)
            }
            INCORRECT_REQUEST -> {
                FilterResult.Error(Errors.IncorrectRequest)
            }
            else -> {
                FilterResult.Error(Errors.ServerError)
            }
        }
    }

    override suspend fun getIndustries(): FilterResult {
        val options = hashMapOf(
            Pair("locale", "RU"),
            Pair("host", "hh.ru")
        )
        val response = networkClient.doRequest(IndustryRequestDto(options))
        return when (response.resultCode) {
            SUCCESS -> {
                val resultList = (response as IndustriesResponse).industries.map { converter.mapIndustryDto(it) }
                FilterResult.Industries(resultList)
            }
            CONNECTION_ERROR -> {
                FilterResult.Error(Errors.ConnectionError)
            }
            INCORRECT_REQUEST -> {
                FilterResult.Error(Errors.IncorrectRequest)
            }
            else -> {
                FilterResult.Error(Errors.ServerError)
            }
        }
    }
}
