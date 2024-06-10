package ru.practicum.android.diploma.favorites.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.practicum.android.diploma.favorites.data.db.dao.VacancyDao
import ru.practicum.android.diploma.favorites.data.db.entity.VacancyEntity

@Database(version = 1, entities = [VacancyEntity::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun vacancyDao(): VacancyDao
}
