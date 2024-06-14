package ru.practicum.android.diploma.favorites.domain.models

data class VacancyDetailsDB(
    val id: String,
    val name: String,
    val currency: String?,
    val salaryFrom: Long?,
    val salaryTo: Long?,
    val employerName: String?,
    val employerLogo: String?,
    val areaName: String,
    val experience: String?,
    val employment: String?,
    val schedule: String?,
    val description: String?,
    val keySkills: List<String>,
    val contactName: String?,
    val contactEmail: String?,
    val contactPhone: List<String?>?,
    val comment: List<String?>?,
)
