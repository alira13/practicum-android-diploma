package ru.practicum.android.diploma.search.data.dto

data class ItemDto(
    val id: String,
    val name: String,
    val area: AreaDto,
    val employer: EmployerDto,
    val salary: SalaryDto?,
)
