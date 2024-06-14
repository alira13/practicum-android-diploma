package ru.practicum.android.diploma.vacancy.domain.models

data class VacancyDetailsR(
    val id: String,
    val name: String,
    val alternateUrl: String,
    val currency: String?,
    val salaryFrom: Long?,
    val salaryTo: Long?,
    val employerName: String?,
    val employerLogo: String?,
    val areaName: String,
    val address: String?,
    val experience: String?,
    val employment: String?,
    val schedule: String?,
    val description: String?,
    val keySkills: String?,
    val contactName: String?,
    val contactEmail: String?,
    val contactPhone: String?,
    val comment: String?
)
