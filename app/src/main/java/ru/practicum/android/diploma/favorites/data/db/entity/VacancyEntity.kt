package ru.practicum.android.diploma.favorites.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vacancy_table")
data class VacancyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val vacancyId: String,
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
    val keySkills: String,
    val contactName: String?,
    val contactEmail: String?,
    val contactPhone: String?,
    val comment: String?,
)
