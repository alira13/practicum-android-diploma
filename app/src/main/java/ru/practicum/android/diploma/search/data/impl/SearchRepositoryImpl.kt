package ru.practicum.android.diploma.search.data.impl

import ru.practicum.android.diploma.search.data.CONNECTION_ERROR
import ru.practicum.android.diploma.search.data.INCORRECT_REQUEST
import ru.practicum.android.diploma.search.data.SUCCESS
import ru.practicum.android.diploma.search.data.VacancyConverter
import ru.practicum.android.diploma.search.data.api.NetworkClient
import ru.practicum.android.diploma.search.data.dto.VacancySearchRequest
import ru.practicum.android.diploma.search.data.dto.reponse.VacanciesResponse
import ru.practicum.android.diploma.search.domain.api.SearchRepository
import ru.practicum.android.diploma.search.domain.models.Errors
import ru.practicum.android.diploma.search.domain.models.SearchResult
import ru.practicum.android.diploma.search.domain.models.VacanciesSearchRequest
import ru.practicum.android.diploma.util.Resource

class SearchRepositoryImpl(
    private val networkClient: NetworkClient,
    private val converter: VacancyConverter
) : SearchRepository {
    override suspend fun searchVacancies(request: VacanciesSearchRequest): Resource<SearchResult> {
        val options: HashMap<String, String> = HashMap()
        options["text"] = request.searchString

        val response = networkClient.doRequest(
            VacancySearchRequest(request.page, options)
        )

        return when (response.resultCode) {
            CONNECTION_ERROR -> {
                Resource.Error(Errors.ConnectionError)
            }

            SUCCESS -> {
                Resource.Success(converter.map(response as VacanciesResponse))
            }

            INCORRECT_REQUEST -> {
                Resource.Error(Errors.IncorrectRequest)
            }

            else -> {
                Resource.Error(Errors.ServerError)
            }
        }
    }
}
