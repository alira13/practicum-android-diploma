package ru.practicum.android.diploma.search.domain.models

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
    val name: String
)
data class Salary(
    val currency: String,
    val from: Long,
    val to: Long
)
