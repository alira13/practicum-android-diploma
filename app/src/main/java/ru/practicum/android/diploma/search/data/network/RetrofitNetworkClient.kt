package ru.practicum.android.diploma.search.data.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.search.data.api.HHApiService
import ru.practicum.android.diploma.search.data.api.NetworkClient
import ru.practicum.android.diploma.search.data.dto.Response
import ru.practicum.android.diploma.search.data.dto.VacancySearchRequest

class RetrofitNetworkClient(
    private val apiService: HHApiService
): NetworkClient {
    override suspend fun doRequest(dto: Any): Response {

        if (dto !is VacancySearchRequest) {
            return Response().apply { resultCode = 400 }
        }

        return withContext(Dispatchers.IO) {
            try {
                val options: HashMap<String, String> = HashMap()
                options["text"] = dto.expression
                val response = apiService.searchVacancies(0, options = options)
                response.apply { resultCode = 200 }
            } catch (e: Throwable) {
                Response().apply { resultCode = 500 }
            }
        }
    }
}
