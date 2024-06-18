package ru.practicum.android.diploma.favorites.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.models.Errors
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetails

interface FavoriteInteractor {
    suspend fun insertFavoriteVacancy(vacancy: VacancyDetails)
    suspend fun deleteFavoriteVacancyById(vacancyId: String)
    suspend fun getListFavoriteVacancies(): Flow<List<VacancyDetails>?>
    suspend fun getFavoriteVacancyById(vacancyId: String): Pair<VacancyDetails?, Errors?>
    suspend fun isVacancyFavorite(vacancyId: String): Boolean
}
