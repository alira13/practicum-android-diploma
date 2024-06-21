package ru.practicum.android.diploma.filter.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.filter.domain.api.SettingsInteractor
import ru.practicum.android.diploma.filter.domain.models.Area
import ru.practicum.android.diploma.filter.domain.models.Industry
import ru.practicum.android.diploma.filter.domain.models.Settings
import ru.practicum.android.diploma.filter.domain.models.WriteRequest

class FilterSettingsViewModel(
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    private val _settingsState: MutableLiveData<Settings> = MutableLiveData()
    fun getSettingsState(): LiveData<Settings> = _settingsState

    fun saveSalarySettings(salary: Int) {
        settingsInteractor.write(WriteRequest.WriteSalary(salary))
    }

    fun savaOnlyWithSalary(onlyWithSalary: Boolean) {
        settingsInteractor.write(WriteRequest.WriteOnlyWithSalary(onlyWithSalary))
    }

    fun readSettings() {
        _settingsState.postValue(settingsInteractor.read())
    }

    fun resetSettings() {
        settingsInteractor.clear()
    }

    fun clearPlaceWork() {
        with(settingsInteractor) {
            write(WriteRequest.WriteArea(Area("", "")))
            read()
        }
    }

    fun clearIndustry() {
        with(settingsInteractor) {
            write(WriteRequest.WriteIndustry(Industry("", "")))
            read()
        }
    }
}
