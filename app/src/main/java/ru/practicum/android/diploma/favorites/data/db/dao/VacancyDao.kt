package ru.practicum.android.diploma.favorites.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.practicum.android.diploma.favorites.data.db.entity.VacancyEntity

@Dao
interface VacancyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVacancy(vacancy: VacancyEntity)

    @Delete(entity = VacancyEntity::class)
    suspend fun deleteVacancy(vacancy: VacancyEntity)

    @Query("SELECT * FROM vacancy_table")
    suspend fun getVacancies(): List<VacancyEntity>
}
