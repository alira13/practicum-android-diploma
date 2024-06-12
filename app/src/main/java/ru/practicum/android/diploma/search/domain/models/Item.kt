package ru.practicum.android.diploma.search.domain.models

import ru.practicum.android.diploma.vacancy.domain.models.Employer
import ru.practicum.android.diploma.vacancy.domain.models.Salary

data class Item(
    val id: String,
    val name: String,
    val area: Area,
    val employer: Employer,
    val salary: Salary?,
)
