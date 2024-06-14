package ru.practicum.android.diploma.favorites.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.favorites.domain.api.VacancyRepository
import ru.practicum.android.diploma.favorites.domain.api.VacansyInteractor
import ru.practicum.android.diploma.favorites.domain.models.VacancyDetailsDB

class VacancyInteractorImpl(private val vacancyRepository: VacancyRepository) : VacansyInteractor {
    override suspend fun insertVacancy(vacancy: VacancyDetailsDB) {
        vacancyRepository.insertVacancy(vacancy)
    }

    override suspend fun deleteVacancy(vacancy: VacancyDetailsDB) {
        vacancyRepository.deleteVacancy(vacancy)
    }

    override suspend fun getVacancies(): Flow<List<VacancyDetailsDB>?> {
        return vacancyRepository.getVacancies()
    }

    override suspend fun getVacancyById(vacancyId: String): Flow<VacancyDetailsDB> {
        return vacancyRepository.getVacancyById(vacancyId)
    }
}
