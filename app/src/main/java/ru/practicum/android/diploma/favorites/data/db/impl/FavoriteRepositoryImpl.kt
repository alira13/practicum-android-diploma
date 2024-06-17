package ru.practicum.android.diploma.favorites.data.db.impl

import android.database.SQLException
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.favorites.data.converters.FavoriteConverter
import ru.practicum.android.diploma.favorites.data.db.AppDatabase
import ru.practicum.android.diploma.favorites.data.db.entity.VacancyEntity
import ru.practicum.android.diploma.favorites.domain.api.FavoriteRepository
import ru.practicum.android.diploma.search.domain.models.Errors
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetails

class FavoriteRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val favoriteConverter: FavoriteConverter
) : FavoriteRepository {

    override suspend fun insertFavoriteVacancy(vacancy: VacancyDetails) {
        val vacancyEntity = favoriteConverter.mapModelToEntity(vacancy)
        appDatabase.vacancyDao().insertVacancy(vacancyEntity)
    }

    override suspend fun deleteFavoriteVacancyById(vacancyId: String) {
        appDatabase.vacancyDao().deleteVacancyById(vacancyId)
    }

    override suspend fun getListFavoriteVacancies(): Flow<List<VacancyDetails>> = flow {
        val vacanciesEntity = appDatabase.vacancyDao().getListFavoritesVacancies()
        emit(mapListEntityToListModel(vacanciesEntity))
    }

    override suspend fun getFavoriteVacancyById(vacancyId: String): Resource<VacancyDetails> {
        val vacancyEntity = appDatabase.vacancyDao().getFavoriteVacancyById(vacancyId)
        return try {
            Resource.Success(favoriteConverter.mapEntityToModel(vacancyEntity))
        } catch (e: SQLException) {
            Log.e("SQLException", "Exception message: ${e.message}")
            Log.e("SQLException", "Exception cause: ${e.cause}")
            Resource.Error(Errors.ServerError, null)
        }
    }

    private fun mapListEntityToListModel(vacanciesEntity: List<VacancyEntity>): List<VacancyDetails> {
        return vacanciesEntity.map { vacancyEntity ->
            favoriteConverter.mapEntityToModel(vacancyEntity)
        }
    }
}
