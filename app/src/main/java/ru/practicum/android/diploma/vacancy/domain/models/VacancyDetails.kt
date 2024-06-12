package ru.practicum.android.diploma.vacancy.domain.models

import ru.practicum.android.diploma.search.domain.models.Area

data class VacancyDetails(
    val id: String,
    val name: String,
    val employer: Employer?,
    val alternateUrl: String,
    val area: Area,
    val experience: Experience?,
    val salary: Salary?,
    val schedule: Schedule,
    val employment: Employment?,
    val description: String,
    val keySkills: List<Skill>,
    val professionalRoles: List<Role>,
    val contacts: Contacts?
)
