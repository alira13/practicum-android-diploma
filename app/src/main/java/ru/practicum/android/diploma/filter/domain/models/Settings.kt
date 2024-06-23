package ru.practicum.android.diploma.filter.domain.models

data class Settings(
    val industry: Industry,
    val country: Country,
    val area: Area,
    val salary: Long,
    val onlyWithSalary: Boolean,
    val filterOn: Boolean
)
