package ru.practicum.android.diploma.search.data.dto.reponse

import com.google.gson.annotations.SerializedName

data class VacanciesResponse(
    val items: List<ItemDto>,
    val found: Long,
    val page: Long,
    val pages: Long,
    @SerializedName("per_page")
    val perPage: Long
) : Response()
