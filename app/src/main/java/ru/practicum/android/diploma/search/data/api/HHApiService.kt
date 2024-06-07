package ru.practicum.android.diploma.search.data.api

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import ru.practicum.android.diploma.search.data.dto.VacanciesResponse

const val ACCESS_TOKEN = "APPLONQV1IU0EMQ20BGHMMFQ7RH58NG65JO5ES56CAADBDL15MCBHUE2HPSI6UPV"
const val APP_NAME = "Vacancy Catcher (n.v.manzha@gmail.com)"
const val ELEMENTS_COUNT = 20
interface HHApiService {
    @Headers(
        "Authorization: Bearer $ACCESS_TOKEN",
        "HH-User-Agent: $APP_NAME"
    )
    @GET("/vacancies")
    suspend fun searchVacancies(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = ELEMENTS_COUNT,
        @Query("search_field") searchField: String
    ): VacanciesResponse
}
