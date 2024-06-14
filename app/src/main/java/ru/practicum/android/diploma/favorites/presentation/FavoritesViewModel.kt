package ru.practicum.android.diploma.favorites.presentation

import android.database.SQLException
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.favorites.domain.api.VacansyInteractor
import ru.practicum.android.diploma.favorites.domain.models.VacancyDetailsDB
import ru.practicum.android.diploma.search.domain.models.VacancyPreview

class FavoritesViewModel(private val interactor: VacansyInteractor) : ViewModel() {

    private val _uiState = MutableLiveData<FavoritesUiState>()
    val uiStateFlow = _uiState

    init {
        //addToFav()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                interactor.getVacancies().collect { vacancies ->
                    when {
                        vacancies == null -> _uiState.postValue(FavoritesUiState.Failure)
                        vacancies.isEmpty() -> _uiState.postValue(FavoritesUiState.Empty)
                        else -> _uiState.postValue(FavoritesUiState.Content(vacancies.map { item -> convert(item) }))
                    }
                }
            } catch (e: SQLException) {
                _uiState.postValue(FavoritesUiState.Failure)
            }
        }
    }

    private fun convert(vacancyDetails: VacancyDetailsDB): VacancyPreview {
        return VacancyPreview(
            id = vacancyDetails.id,
            iconUrl = vacancyDetails.employerLogo.toString(),
            description = vacancyDetails.name,
            employer = vacancyDetails.employerName,
            salary = vacancyDetails.salaryTo
        )
    }

    private fun convert(vacancyDetails: VacancyPreview): VacancyDetailsDB {
        return VacancyDetailsDB(
            vacancyDetails.id,
            vacancyDetails.description,
            "",
            vacancyDetails.salary,
            vacancyDetails.salary,
            vacancyDetails.employer,
            vacancyDetails.iconUrl,
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
        )
    }

    fun addToFav() {
        viewModelScope.launch {
            val fav = VacancyPreview(
                "11",
                null,
                "Мегамозг, возраст 25 лет, опыт 15",
                "Сказошный наниматель",
                "ну где-то около 20 000 руб."
            )
            interactor.insertVacancy(convert(fav))
        }
    }

    fun deleteFromFavorites(vacancy: VacancyDetailsDB) {
        viewModelScope.launch {
            interactor.deleteVacancy(vacancy)
        }
    }
}
