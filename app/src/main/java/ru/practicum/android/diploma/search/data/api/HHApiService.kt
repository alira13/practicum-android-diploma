package ru.practicum.android.diploma.search.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.filter.data.dto.IndustryDto
import ru.practicum.android.diploma.filter.data.dto.RegionDto
import ru.practicum.android.diploma.search.data.dto.reponse.VacanciesResponse
import ru.practicum.android.diploma.vacancy.data.dto.response.VacancyDetailsResponse

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
    ): Response<VacancyDetailsResponse>

    @Headers(
        "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: $APP_NAME"
    )
    @GET("/areas")
    suspend fun getRegions(
        @QueryMap options: Map<String, String>
    ): List<RegionDto>

    @Headers(
        "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: $APP_NAME"
    )
    @GET("/areas")
    suspend fun getIndustries(
        @QueryMap options: Map<String, String>
    ): List<IndustryDto>
}
