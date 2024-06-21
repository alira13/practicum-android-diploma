package ru.practicum.android.diploma.search.data.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.filter.data.dto.IndustriesResponse
import ru.practicum.android.diploma.filter.data.dto.IndustryRequestDto
import ru.practicum.android.diploma.filter.data.dto.RegionsRequestDto
import ru.practicum.android.diploma.filter.data.dto.RegionsResponse
import ru.practicum.android.diploma.search.data.CONNECTION_ERROR
import ru.practicum.android.diploma.search.data.ERROR_404
import ru.practicum.android.diploma.search.data.INCORRECT_REQUEST
import ru.practicum.android.diploma.search.data.SUCCESS
import ru.practicum.android.diploma.search.data.api.HHApiService
import ru.practicum.android.diploma.search.data.api.NetworkClient
import ru.practicum.android.diploma.search.data.dto.VacancySearchRequest
import ru.practicum.android.diploma.search.data.dto.reponse.Response
import ru.practicum.android.diploma.util.isConnected
import ru.practicum.android.diploma.vacancy.data.dto.VacancyDetailsRequestDto
import ru.practicum.android.diploma.vacancy.data.dto.response.VacancyDetailsResponse
import java.io.IOException

class RetrofitNetworkClient(
    private val apiService: HHApiService
) : NetworkClient {
    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = CONNECTION_ERROR }
        }

        return withContext(Dispatchers.IO) {
            try {
                when (dto) {
                    is VacancySearchRequest -> {
                        val response = apiService.searchVacancies(dto.page, options = dto.options)
                        response.apply { resultCode = SUCCESS }
                    }

                    is VacancyDetailsRequestDto -> {
                        val response = apiService.getVacancyDetails(dto.id, options = dto.options)
                        val detailResponse = if (response.isSuccessful) {
                            response.body() as VacancyDetailsResponse
                        } else {
                            val code = response.code()
                            Response().apply { resultCode = code }
                        }
                        detailResponse
                    }

                    is RegionsRequestDto -> {
                        val regions = apiService.getRegions(options = dto.options)
                        val response = RegionsResponse(regions)
                        response.apply { resultCode = SUCCESS }
                    }

                    is IndustryRequestDto -> {
                        val industries = apiService.getIndustries(options = dto.options)
                        val response = IndustriesResponse(industries)
                        response.apply { resultCode = SUCCESS }
                    }

                    else -> {
                        Response().apply { resultCode = INCORRECT_REQUEST }
                    }
                }
            } catch (e: IOException) {
                Log.e("RetrofitNetworkClient", "exception handled $e")
                Response().apply { resultCode = ERROR_404 }
            }
        }
    }
}
