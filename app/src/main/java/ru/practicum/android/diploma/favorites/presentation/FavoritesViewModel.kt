package ru.practicum.android.diploma.favorites.presentation

import android.database.SQLException
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.favorites.domain.api.FavoriteInteractor
import ru.practicum.android.diploma.search.domain.models.VacancyPreview
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetails

class FavoritesViewModel(private val interactor: FavoriteInteractor) : ViewModel() {

    private val _uiState = MutableLiveData<FavoritesUiState>()
    val uiStateFlow = _uiState

    fun getListFavoriteVacancies() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                interactor.getListFavoriteVacancies().collect { vacancies ->
                    when {
                        vacancies == null -> _uiState.postValue(FavoritesUiState.Failure)
                        vacancies.isEmpty() -> _uiState.postValue(FavoritesUiState.Empty)
                        else -> _uiState.postValue(FavoritesUiState.Content(vacancies.map { item -> convert(item) }))
                    }
                }
            } catch (e: SQLException) {
                _uiState.postValue(FavoritesUiState.Failure)
                Log.e("AppHandledExceptionTag", e.stackTraceToString())
            }
        }
    }

    private fun convert(vacancyDetails: VacancyDetails): VacancyPreview {
        return VacancyPreview(
            id = vacancyDetails.id,
            iconUrl = vacancyDetails.logoUrls,
            description = vacancyDetails.name,
            employer = vacancyDetails.employer,
            salary = vacancyDetails.salary
        )
    }
}
