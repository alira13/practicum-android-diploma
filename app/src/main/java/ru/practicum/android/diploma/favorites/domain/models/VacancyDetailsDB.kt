package ru.practicum.android.diploma.favorites.domain.models

data class VacancyDetailsDB(
    val id: String,
    val name: String,
    val currency: String,
    val salaryFrom: String,
    val salaryTo: String,
    val employerName: String,
    val employerLogo: String?,
    val areaName: String,
    val experience: String,
    val employment: String,
    val schedule: String,
    val description: String,
    val keySkills: String,
    val contactName: String,
    val contactEmail: String,
    val contactPhone: String,
    val comment: String,
)
