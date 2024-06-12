package ru.practicum.android.diploma.vacancy.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.search.domain.models.Errors
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailsInteractor
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetails
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetailsRequest
import ru.practicum.android.diploma.vacancy.ui.models.VacancyDetailsUIState

class VacancyDetailsViewModel(
    id: String,
    private val vacancyDetailsInteractor: VacancyDetailsInteractor
) : ViewModel() {

    private val vacancyDetailsState: MutableLiveData<VacancyDetailsUIState> = MutableLiveData()
    fun getUIState(): LiveData<VacancyDetailsUIState> = vacancyDetailsState

    init {
        getVacancyDetails(id)
    }

    private fun getVacancyDetails(id: String) {
        vacancyDetailsState.value = VacancyDetailsUIState.Loading
        viewModelScope.launch {
            val result = vacancyDetailsInteractor.getVacancyDetails(
                VacancyDetailsRequest(id = id)
            )
            processResult(result.first, result.second)
        }
    }

    private fun processResult(details: VacancyDetails?, errors: Errors?) {
        if (details != null) {
            vacancyDetailsState.value = VacancyDetailsUIState.Content(details)
        } else {
            vacancyDetailsState.value = VacancyDetailsUIState.Error(errors)
        }
    }
}
