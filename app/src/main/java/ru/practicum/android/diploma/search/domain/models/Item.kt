package ru.practicum.android.diploma.search.domain.models

import ru.practicum.android.diploma.search.data.dto.LogoUrls

data class Item(
    val id: String,
    val name: String,
    val area: Area,
    val employer: Employer,
    val salary: Salary?,
)

data class Area(
    val id: String,
    val name: String,
)
data class Employer(
    val id: String,
    val name: String,
    val logoUrls: LogoUrls?
)
data class Salary(
    val currency: String,
    val from: Long,
    val to: Long
)
