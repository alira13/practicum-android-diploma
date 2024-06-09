package ru.practicum.android.diploma.vacancy.domain.models

import ru.practicum.android.diploma.search.data.dto.LogoUrls
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

data class Schedule(
    val id: String?,
    val name: String
)

data class Experience(
    val id: String,
    val name: String
)

data class Employment(
    val id: String?,
    val name: String
)

data class Role(
    val id: String,
    val name: String
)

data class Skill(
    val name: String
)

data class Contacts(
    val email: String?,
    val name: String?,
    val phones: List<Phone>?
)

data class Phone(
    val city: String,
    val comment: String?,
    val country: String,
    val formatted: String,
    val number: String
)

