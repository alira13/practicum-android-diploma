package ru.practicum.android.diploma.search.data.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.practicum.android.diploma.search.data.api.HHApiService
import ru.practicum.android.diploma.search.data.api.NetworkClient
import ru.practicum.android.diploma.search.data.dto.VacancySearchRequest
import ru.practicum.android.diploma.search.data.dto.reponse.Response
import ru.practicum.android.diploma.util.isConnected
import ru.practicum.android.diploma.vacancy.data.dto.VacancyDetailsRequestDto

class RetrofitNetworkClient(
    private val apiService: HHApiService,
) : NetworkClient {
    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            Response().apply { resultCode = CONNECTION_ERROR }
        }

        return getResponse(dto)
    }

    private suspend fun getResponse(dto: Any): Response {
        return withContext(Dispatchers.IO) {
            try {
                when (dto) {
                    is VacancySearchRequest -> {
                        val response = apiService.searchVacancies(dto.page, options = dto.options)
                        response.apply { resultCode = SUCCESS }
                    }

                    is VacancyDetailsRequestDto -> {
                        val response = apiService.getVacancyDetails(dto.id, options = dto.options)
                        response.apply { resultCode = SUCCESS }
                    }

                    else -> {
                        Response().apply { resultCode = INCORRECT_REQUEST }
                    }
                }

            } catch (e: HttpException) {
                println("exception handled $e")
                Response().apply { resultCode = SERVER_ERROR }
            }
        }
    }

    companion object {
        const val INCORRECT_REQUEST = 400
        const val SUCCESS = 200
        const val SERVER_ERROR = 500
        const val CONNECTION_ERROR = -1
    }
}
