package ru.practicum.android.diploma.search.data.api

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.search.data.dto.VacanciesResponse
import ru.practicum.android.diploma.vacancy.data.dto.VacancyDetailsResponse

const val APP_NAME = "Vacancy Catcher (n.v.manzha@gmail.com)"
const val ELEMENTS_COUNT = 20
interface HHApiService {
    @Headers(
        "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: $APP_NAME"
    )
    @GET("/vacancies")
    suspend fun searchVacancies(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = ELEMENTS_COUNT,
        @QueryMap options: Map<String, String>
    ): VacanciesResponse
    @Headers(
        "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: $APP_NAME"
    )
    @GET("/vacancies/{id}")
    suspend fun getVacancyDetails(
        @Path("id") id: String,
        @QueryMap options: Map<String, String>
    ): VacancyDetailsResponse
}
