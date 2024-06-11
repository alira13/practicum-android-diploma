package ru.practicum.android.diploma.favorites.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vacancy_table")
data class VacancyEntity(
    @PrimaryKey
    val id: Long,
    val billingName: String,
    val areaName: String,
    val relationsName: String,
    val salary: String,
    val experience: String,
    val schedule: String,
    val employment: String,
    val contacts: String,
    val description: String,
    val keySkills: String,
    val logoUrls: String
)
