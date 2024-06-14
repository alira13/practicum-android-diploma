package ru.practicum.android.diploma.favorites.data.db.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.favorites.data.converters.VacancyConverterDB
import ru.practicum.android.diploma.favorites.data.db.AppDatabase
import ru.practicum.android.diploma.favorites.data.db.entity.VacancyEntity
import ru.practicum.android.diploma.favorites.domain.api.VacancyRepository
import ru.practicum.android.diploma.favorites.domain.models.VacancyDetailsDB

class VacancyRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val vacancyConverter: VacancyConverterDB
) : VacancyRepository {

    override suspend fun insertVacancy(vacancy: VacancyDetailsDB) {
        val vacancyEntity = vacancyConverter.mapModelToEntity(vacancy)
        appDatabase.vacancyDao().insertVacancy(vacancyEntity)
    }

    override suspend fun deleteVacancy(vacancy: VacancyDetailsDB) {
        val vacancyEntity = vacancyConverter.mapModelToEntity(vacancy)
        appDatabase.vacancyDao().deleteVacancy(vacancyEntity)
    }

    override suspend fun getVacancies(): Flow<List<VacancyDetailsDB>> = flow {
        val vacanciesEntity = appDatabase.vacancyDao().getVacancies()
        emit(mapListEntityToListModel(vacanciesEntity))
    }

    override suspend fun getVacancyById(vacancyId: String): Flow<VacancyDetailsDB> = flow {
        val vacancyEntity = appDatabase.vacancyDao().getVacancyById(vacancyId)
        emit(vacancyConverter.mapEntityToModel(vacancyEntity))
    }

    private fun mapListEntityToListModel(vacanciesEntity: List<VacancyEntity>): List<VacancyDetailsDB> {
        return vacanciesEntity.map { vacancyEntity ->
            vacancyConverter.mapEntityToModel(vacancyEntity)
        }
    }
}
