package ru.practicum.android.diploma.filter.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.filter.domain.api.SettingsInteractor
import ru.practicum.android.diploma.filter.domain.models.Area
import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.filter.domain.models.Industry
import ru.practicum.android.diploma.filter.domain.models.Settings
import ru.practicum.android.diploma.filter.domain.models.WriteRequest

class FilterSettingsViewModel(
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    private val _salaryState: MutableLiveData<Settings> = MutableLiveData()
    fun getSalaryState(): LiveData<Settings> = _salaryState

    private val _placeWorkState: MutableLiveData<String> = MutableLiveData()
    fun getPlaceWorkState(): LiveData<String> = _placeWorkState

    private val _industryState: MutableLiveData<Settings> = MutableLiveData()
    fun getIndustryState(): LiveData<Settings> = _industryState

    fun saveSalarySettings(salary: Long) {
        settingsInteractor.write(WriteRequest.WriteSalary(salary))
    }

    fun savaOnlyWithSalary(onlyWithSalary: Boolean) {
        settingsInteractor.write(WriteRequest.WriteOnlyWithSalary(onlyWithSalary))
    }

    fun saveFilterSettings() {
        val filterSettings = settingsInteractor.read()
        val filterOn = isSettingsEmpty(filterSettings)
        settingsInteractor.write(WriteRequest.WriteFilterOn(filterOn))
    }

    fun readSettings() {
        _salaryState.postValue(settingsInteractor.read())
        _placeWorkState.postValue(formatterPlaceWork(settingsInteractor.read()))
        _industryState.postValue(settingsInteractor.read())
    }

    fun resetSettings() {
        settingsInteractor.clear()
    }

    fun clearPlaceWork() {
        with(settingsInteractor) {
            write(WriteRequest.WriteCountry(Country(EMPTY_VALUE, EMPTY_VALUE)))
            write(WriteRequest.WriteArea(Area(EMPTY_VALUE, EMPTY_VALUE, EMPTY_VALUE)))
            _placeWorkState.postValue(formatterPlaceWork(read()))
        }
    }

    fun clearIndustry() {
        with(settingsInteractor) {
            write(WriteRequest.WriteIndustry(Industry("", "")))
            _industryState.postValue(settingsInteractor.read())
        }
    }

    private fun isSettingsEmpty(filterSettings: Settings): Boolean {
        return !(!filterSettings.onlyWithSalary &&
            filterSettings.salary == 0L &&
            filterSettings.area.id.isEmpty() &&
            filterSettings.country.id.isEmpty() &&
            filterSettings.industry.id.isEmpty())
    }

    private fun formatterPlaceWork(settingsState: Settings): String {
        var placeWorkValue = EMPTY_VALUE
        settingsState.apply {
            if (country.id.isNotEmpty() && area.id.isNotEmpty()) {
                placeWorkValue = "${country.name}, ${area.name}"
            }
            if (country.id.isNotEmpty() && area.id.isEmpty()) {
                placeWorkValue = country.name
            }
            if (country.id.isEmpty() && area.id.isNotEmpty()) {
                placeWorkValue = area.name
            }
        }
        return placeWorkValue
    }

    companion object {
        const val EMPTY_VALUE = ""
    }
}
