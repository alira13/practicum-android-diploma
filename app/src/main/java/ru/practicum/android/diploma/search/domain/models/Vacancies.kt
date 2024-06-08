package ru.practicum.android.diploma.search.domain.models

data class Vacancies(
    val items: List<Item>,
    val found: Long,
    val page: Long,
    val pages: Long,
    val perPage: Long
)
