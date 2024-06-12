package ru.practicum.android.diploma.vacancy.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailsInteractor
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetailsRequest
import ru.practicum.android.diploma.vacancy.ui.models.VacancyDetailsUIState

class VacancyDetailsViewModel(
    private val id: String,
    private val vacancyDetailsInteractor: VacancyDetailsInteractor
) : ViewModel() {

    val vacancyDetailsState: MutableLiveData<VacancyDetailsUIState> = MutableLiveData()
    init {
        getVacancyDetails(id)
    }

    fun getVacancyDetails(
        id: String,
        locale: String = "RU",
        host: String = "hh.ru"
    ) {
        viewModelScope.launch {
            val result = vacancyDetailsInteractor.getVacancyDetails(
                VacancyDetailsRequest(id = id)
            )
        }
    }

    //private fun
}
