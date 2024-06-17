package ru.practicum.android.diploma.favoritedetails.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.favorites.domain.api.FavoriteInteractor
import ru.practicum.android.diploma.share.domain.api.SharingInteractor
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailsInteractor
import ru.practicum.android.diploma.vacancy.presentation.VacancyDetailsViewModel
import ru.practicum.android.diploma.vacancy.ui.models.VacancyDetailsUIState

class FavoriteDetailsViewModel(
    val id: String,
    val vacancyDetailsInteractor: VacancyDetailsInteractor,
    val sharingInteractor: SharingInteractor,
    val favoriteInteractor: FavoriteInteractor
) : VacancyDetailsViewModel(
    id,
    vacancyDetailsInteractor,
    sharingInteractor,
    favoriteInteractor
) {

    init {
        getFavoriteVacancy(id)
    }

    private fun getFavoriteVacancy(id: String) {
        vacancyDetailsState.value = VacancyDetailsUIState.Loading
        viewModelScope.launch {
            val result = favoriteInteractor.getFavoriteVacancyById(id)
            processResult(result.first, result.second)
        }
    }
}
