package ru.practicum.android.diploma.vacancy.data.impl

import ru.practicum.android.diploma.search.data.CONNECTION_ERROR
import ru.practicum.android.diploma.search.data.INCORRECT_REQUEST
import ru.practicum.android.diploma.search.data.SUCCESS
import ru.practicum.android.diploma.search.data.api.NetworkClient
import ru.practicum.android.diploma.search.domain.models.Errors
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.vacancy.data.DetailsConverter
import ru.practicum.android.diploma.vacancy.data.dto.VacancyDetailsRequestDto
import ru.practicum.android.diploma.vacancy.data.dto.VacancyDetailsResponse
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailsRepository
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetails
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetailsRequest

class VacancyDetailsRepositoryImpl(
    private val networkClient: NetworkClient,
    private val converter: DetailsConverter
) : VacancyDetailsRepository {
    override suspend fun getVacancyDetails(request: VacancyDetailsRequest): Resource<VacancyDetails> {
        val options: HashMap<String, String> = HashMap()
        if (request.locale.isNotEmpty())
            options["locale"] = request.locale
        if (request.host.isNotEmpty())
            options["host"] = request.host

        val response = networkClient.doRequest(
            VacancyDetailsRequestDto(request.id, options)
        )

        return when (response.resultCode) {
            CONNECTION_ERROR -> {
                Resource.Error(Errors.ConnectionError)
            }

            SUCCESS -> {
                Resource.Success(converter.map(response as VacancyDetailsResponse))
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
