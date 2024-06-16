package ru.practicum.android.diploma.favorites.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vacancy_table")
data class VacancyEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    val vacancyId: String,
    val name: String,
    val employerName: String?,
    val employerLogo: String?,
    val alternateUrl: String,
    val areaName: String,
    val experience: String?,
    val salary: String?,
    val employment: String?,
    val description: String,
    val keySkills: String?,
    val contactName: String?,
    val contactPhone: String?,
    val contactEmail: String?,
    val comment: String?,
    val isFavorite: Boolean,
)
