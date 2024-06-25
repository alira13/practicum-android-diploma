package ru.practicum.android.diploma.favorites.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.favorites.domain.api.FavoriteInteractor
import ru.practicum.android.diploma.favorites.domain.api.FavoriteRepository
import ru.practicum.android.diploma.search.domain.models.Errors
import ru.practicum.android.diploma.util.Resource
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetails

class FavoriteInteractorImpl(private val favoriteRepository: FavoriteRepository) : FavoriteInteractor {
    override suspend fun insertFavoriteVacancy(vacancy: VacancyDetails) {
        favoriteRepository.insertFavoriteVacancy(vacancy)
    }

    override suspend fun deleteFavoriteVacancyById(vacancyId: String) {
        favoriteRepository.deleteFavoriteVacancyById(vacancyId)
    }

    override suspend fun getListFavoriteVacancies(): Flow<List<VacancyDetails>?> {
        return favoriteRepository.getListFavoriteVacancies()
    }

    override suspend fun getFavoriteVacancyById(vacancyId: String): Pair<VacancyDetails?, Errors?> {
        return when (val result: Resource<VacancyDetails> = favoriteRepository.getFavoriteVacancyById(vacancyId)) {
            is Resource.Success -> Pair(result.data, null)
            is Resource.Error -> Pair(null, result.error)
        }
    }

    override suspend fun isVacancyFavorite(vacancyId: String): Boolean {
        return favoriteRepository.isVacancyFavorite(vacancyId)
    }
}
