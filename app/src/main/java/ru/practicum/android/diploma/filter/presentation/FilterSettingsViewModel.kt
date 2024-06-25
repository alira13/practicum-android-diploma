package ru.practicum.android.diploma.filter.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filter.domain.api.SettingsInteractor
import ru.practicum.android.diploma.filter.domain.models.Area
import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.filter.domain.models.Industry
import ru.practicum.android.diploma.filter.domain.models.Settings
import ru.practicum.android.diploma.filter.domain.models.WriteRequest

class FilterSettingsViewModel(
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    private var savedCountry = EMPTY_COUNTRY
    private var savedArea = EMPTY_AREA
    private var savedIndustry = EMPTY_INDUSTRY
    private var savedSalary = 0L
    private var savedOnlyWithSalary = false


    private val _placeWorkState: MutableLiveData<String> = MutableLiveData()
    fun getPlaceWorkState(): LiveData<String> = _placeWorkState

    private val _industryState: MutableLiveData<Industry> = MutableLiveData()
    fun getIndustryState(): LiveData<Industry> = _industryState

    private val _salaryState: MutableLiveData<Settings> = MutableLiveData()
    fun getSalaryState(): LiveData<Settings> = _salaryState

    private val _onlyWithSalaryState: MutableLiveData<Boolean> = MutableLiveData()
    fun getOnlyWithSalaryState(): LiveData<Boolean> = _onlyWithSalaryState

    init {
        readSavedSettings()
    }

    fun saveSalary(salary: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsInteractor.write(WriteRequest.WriteSalary(salary))
        }
    }

    fun saveOnlyWithSalary(onlyWithSalary: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsInteractor.write(WriteRequest.WriteOnlyWithSalary(onlyWithSalary))
        }
    }

    fun saveFilterSettings() {
        viewModelScope.launch(Dispatchers.IO) {
            val filterSettings = settingsInteractor.read()
            val filterOn = isSettingsEmpty(filterSettings)
            settingsInteractor.write(WriteRequest.WriteFilterOn(filterOn))
        }
    }

    fun returnSavedSettings() {
        viewModelScope.launch(Dispatchers.IO) {
            with(settingsInteractor) {
                write(WriteRequest.WriteCountry(savedCountry))
                write(WriteRequest.WriteArea(savedArea))
                write(WriteRequest.WriteIndustry(savedIndustry))
                write(WriteRequest.WriteSalary(savedSalary))
                write(WriteRequest.WriteOnlyWithSalary(savedOnlyWithSalary))
            }
        }
    }

    fun readSavedSettings() {
        viewModelScope.launch(Dispatchers.IO) {
            with(settingsInteractor) {
                savedCountry = read().country
                savedArea = read().area
                savedIndustry = read().industry
                savedSalary = read().salary
                savedOnlyWithSalary = read().onlyWithSalary
                _salaryState.postValue(read())
                _placeWorkState.postValue(formatterPlaceWork(read()))
                _industryState.postValue(savedIndustry)
                _onlyWithSalaryState.postValue(savedOnlyWithSalary)
            }
        }
    }

    fun readNewSettings() {
            _placeWorkState.postValue(formatterPlaceWork(settingsInteractor.read()))
            _industryState.postValue(settingsInteractor.read().industry)
            _onlyWithSalaryState.postValue(settingsInteractor.read().onlyWithSalary)
            _salaryState.postValue(settingsInteractor.read())
    }

    fun resetSettings() {
        viewModelScope.launch(Dispatchers.IO) {
            settingsInteractor.clear()
        }
    }

    fun clearPlaceWork() {
        viewModelScope.launch(Dispatchers.IO) {
            with(settingsInteractor) {
                write(WriteRequest.WriteCountry(EMPTY_COUNTRY))
                write(WriteRequest.WriteArea(EMPTY_AREA))
                _placeWorkState.postValue(formatterPlaceWork(read()))
            }
        }
    }

    fun clearIndustry() {
        viewModelScope.launch(Dispatchers.IO) {
            with(settingsInteractor) {
                write(WriteRequest.WriteIndustry(EMPTY_INDUSTRY))
                _industryState.postValue(EMPTY_INDUSTRY)
            }
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
        val EMPTY_INDUSTRY = Industry(EMPTY_VALUE, EMPTY_VALUE)
        val EMPTY_AREA = Area(EMPTY_VALUE, EMPTY_VALUE, EMPTY_VALUE)
        val EMPTY_COUNTRY = Country(EMPTY_VALUE, EMPTY_VALUE)
    }
}
