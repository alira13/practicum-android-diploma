package ru.practicum.android.diploma.search.domain.models

data class Vacancies(
    val items: List<Item>,
    val found: Int,
    val page: Int,
    val pages: Int,
    val perPage: Int
)
