package ru.practicum.android.diploma.search.data.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
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
                val options: HashMap<String, String> = HashMap()
                options["text"] = dto.expression
                val response = apiService.searchVacancies(0, options = options)
                response.apply { resultCode = SUCCESS }
            } catch (e: HttpException) {
                Log.e(TAG, "exception handled $e")
                Response().apply { resultCode = SERVER_ERROR }
            }
        }
    }

    companion object {
        const val TAG = "RetrofitNetworkClient"
        const val INCORRECT_REQUEST = 400
        const val SUCCESS = 200
        const val SERVER_ERROR = 500
        const val CONNECTION_ERROR = -1
    }
}
