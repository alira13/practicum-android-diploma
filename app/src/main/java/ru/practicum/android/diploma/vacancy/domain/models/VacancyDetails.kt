package ru.practicum.android.diploma.vacancy.domain.models

data class VacancyDetails(
    val id: String,
    val name: String,
    val employer: String?,
    val logoUrls: String?,
    val alternateUrl: String,
    val area: String,
    val experience: String?,
    val salary: String?,
    val employment: String?,
    val description: String,
    val keySkills: String?,
    val contactName: String?,
    val phone: String?,
    val email: String?,
    val comment: String?
)
