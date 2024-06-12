package ru.practicum.android.diploma.search.data.dto

data class VacancySearchRequest(
    val page: Int,
    val options: Map<String, String>
)
