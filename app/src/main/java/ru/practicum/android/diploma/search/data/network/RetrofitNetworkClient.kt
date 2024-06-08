package ru.practicum.android.diploma.search.data.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.practicum.android.diploma.search.data.CONNECTION_ERROR
import ru.practicum.android.diploma.search.data.INCORRECT_REQUEST
import ru.practicum.android.diploma.search.data.SERVER_ERROR
import ru.practicum.android.diploma.search.data.SUCCESS
import ru.practicum.android.diploma.search.data.api.HHApiService
import ru.practicum.android.diploma.search.data.api.NetworkClient
import ru.practicum.android.diploma.search.data.dto.Response
import ru.practicum.android.diploma.search.data.dto.VacancySearchRequest
import ru.practicum.android.diploma.util.isConnected

class RetrofitNetworkClient(
    private val apiService: HHApiService
) : NetworkClient {
    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = CONNECTION_ERROR }
        }

        if (dto !is VacancySearchRequest) {
            return Response().apply { resultCode = INCORRECT_REQUEST }
        }

        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.searchVacancies(dto.page, options = dto.options)
                response.apply { resultCode = SUCCESS }
            } catch (e: HttpException) {
                Log.e(TAG, "exception handled $e")
                Response().apply { resultCode = SERVER_ERROR }
            }
        }
    }

    companion object {
        const val TAG = "RetrofitNetworkClient"
    }
}
