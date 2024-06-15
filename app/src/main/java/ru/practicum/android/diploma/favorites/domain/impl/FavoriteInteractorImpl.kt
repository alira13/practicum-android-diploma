
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

    override suspend fun getFavoriteVacancyById(vacancyId: String): Flow<VacancyDetails> {
        return favoriteRepository.getFavoriteVacancyById(vacancyId)
    }
}
