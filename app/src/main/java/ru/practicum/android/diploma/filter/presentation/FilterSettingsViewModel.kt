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

    /*private var savedCountry = EMPTY_COUNTRY
    private var savedArea = EMPTY_AREA
    private var savedIndustry = EMPTY_INDUSTRY
    private var savedSalary = 0L
    private var savedOnlyWithSalary = false*/
    private lateinit var savedSettings: Settings


    private val _placeWorkState: MutableLiveData<String> = MutableLiveData()
    fun getPlaceWorkState(): LiveData<String> = _placeWorkState

    private val _industryState: MutableLiveData<Industry> = MutableLiveData()
    fun getIndustryState(): LiveData<Industry> = _industryState

    private val _salaryState: MutableLiveData<Long> = MutableLiveData()
    fun getSalaryState(): LiveData<Long> = _salaryState

    private val _onlyWithSalaryState: MutableLiveData<Boolean> = MutableLiveData()
    fun getOnlyWithSalaryState(): LiveData<Boolean> = _onlyWithSalaryState

    private val _buttonState: MutableLiveData<Boolean> = MutableLiveData()
    fun getButtonState(): LiveData<Boolean> = _buttonState

    init {
        readSavedSettings()
    }

    fun saveSalary(salary: Long) {
        settingsInteractor.write(WriteRequest.WriteSalary(salary), SETTINGS_KEY)
    }

    fun saveOnlyWithSalary(onlyWithSalary: Boolean) {
        settingsInteractor.write(WriteRequest.WriteOnlyWithSalary(onlyWithSalary), SETTINGS_KEY)
    }

    fun saveFilterOnState() {
        val filterSettings = settingsInteractor.read(SETTINGS_KEY)
        val filterOn = isSettingsEmpty(filterSettings)
        settingsInteractor.write(WriteRequest.WriteFilterOn(filterOn), APPLIED_SETTINGS_KEY)
    }

    fun returnSavedSettings() {
        with(settingsInteractor) {
            write(WriteRequest.WriteCountry(savedSettings.country), SETTINGS_KEY)
            write(WriteRequest.WriteArea(savedSettings.area), SETTINGS_KEY)
            write(WriteRequest.WriteIndustry(savedSettings.industry), SETTINGS_KEY)
            write(WriteRequest.WriteSalary(savedSettings.salary), SETTINGS_KEY)
            write(WriteRequest.WriteOnlyWithSalary(savedSettings.onlyWithSalary), SETTINGS_KEY)
        }
    }

    private fun readSavedSettings() {
        savedSettings = settingsInteractor.read(SETTINGS_KEY)
    }

    fun readNewSettings() {
        val newFilterSettings = settingsInteractor.read(SETTINGS_KEY)
        _placeWorkState.postValue(formatterPlaceWork(newFilterSettings))
        _industryState.postValue(newFilterSettings.industry)
        _onlyWithSalaryState.postValue(newFilterSettings.onlyWithSalary)
        _salaryState.postValue(newFilterSettings.salary)
        _buttonState.postValue(compareValueSettings(newFilterSettings) && isSettingsEmpty(newFilterSettings))
        settingsInteractor.write(WriteRequest.WriteSettings(newFilterSettings), SETTINGS_KEY)
    }

    private fun compareValueSettings(newSettings: Settings): Boolean {
        return savedSettings != newSettings
    }

    fun resetSettings() {
        settingsInteractor.clear()
    }

    fun buttonGroupOn() {
        _buttonState.postValue(true)
    }

    fun clearPlaceWork() {
        with(settingsInteractor) {
            write(WriteRequest.WriteCountry(EMPTY_COUNTRY), SETTINGS_KEY)
            write(WriteRequest.WriteArea(EMPTY_AREA), SETTINGS_KEY)
            _placeWorkState.postValue(formatterPlaceWork(read(SETTINGS_KEY)))
        }
    }

    fun clearIndustry() {
        with(settingsInteractor) {
            write(WriteRequest.WriteIndustry(EMPTY_INDUSTRY), SETTINGS_KEY)
            _industryState.postValue(EMPTY_INDUSTRY)
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
        const val SETTINGS_KEY = "settings key"
        const val APPLIED_SETTINGS_KEY = "applied settings key"
    }
}
