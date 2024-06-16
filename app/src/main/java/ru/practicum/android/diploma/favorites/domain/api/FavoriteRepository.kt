package ru.practicum.android.diploma.favorites.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetails

interface FavoriteRepository {
    suspend fun insertFavoriteVacancy(vacancy: VacancyDetails)
    suspend fun deleteFavoriteVacancyById(vacancyId: String)
    suspend fun getListFavoriteVacancies(): Flow<List<VacancyDetails>?>
    suspend fun getFavoriteVacancyById(vacancyId: String): Flow<VacancyDetails>
}
