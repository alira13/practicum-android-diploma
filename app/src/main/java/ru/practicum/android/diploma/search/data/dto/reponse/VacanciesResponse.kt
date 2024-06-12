package ru.practicum.android.diploma.search.data.dto.reponse

import com.google.gson.annotations.SerializedName

data class VacanciesResponse(
    val items: List<ItemDto>,
    val found: Int,
    val page: Int,
    val pages: Int,
    @SerializedName("per_page")
    val perPage: Int
) : Response()
