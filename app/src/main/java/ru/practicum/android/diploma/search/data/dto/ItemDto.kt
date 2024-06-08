package ru.practicum.android.diploma.search.data.dto

data class ItemDto(
    val id: String,
    val name: String,
    val area: AreaDto,
    val employer: EmployerDto,
    val salary: SalaryDto?,
)

data class AreaDto(
    val id: String,
    val name: String,
)
data class EmployerDto(
    val id: String,
    val name: String
)
data class SalaryDto(
    val currency: String,
    val from: Long,
    val to: Long
)
