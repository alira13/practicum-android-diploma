package ru.practicum.android.diploma.favorites.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.practicum.android.diploma.favorites.data.db.entity.VacancyEntity

@Dao
interface VacancyDao {

    @Insert(
        entity = VacancyEntity::class,
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertVacancy(vacancy: VacancyEntity)

    @Query("DELETE FROM vacancy_table WHERE vacancyId = :vacancyId")
    suspend fun deleteVacancyById(vacancyId: String)

    @Query("SELECT * FROM vacancy_table")
    suspend fun getListFavoritesVacancies(): List<VacancyEntity>

    @Query("SELECT * FROM vacancy_table WHERE id = :vacancyId")
    suspend fun getFavoriteVacancyById(vacancyId: String): VacancyEntity

    @Query("SELECT vacancyId FROM vacancy_table")
    suspend fun getListIdFavoriteVacancies(): List<String>

    @Update(
        entity = VacancyEntity::class,
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun updateVacancy(vacancy: VacancyEntity)
}
