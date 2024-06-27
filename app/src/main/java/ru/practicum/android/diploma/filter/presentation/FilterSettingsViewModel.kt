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

    private var savedCountry = EMPTY_COUNTRY
    private var savedArea = EMPTY_AREA
    private var savedIndustry = EMPTY_INDUSTRY
    private var savedSalary = 0L
    private var savedOnlyWithSalary = false

    private val _placeWorkState: MutableLiveData<String> = MutableLiveData()
    fun getPlaceWorkState(): LiveData<String> = _placeWorkState

    private val _industryState: MutableLiveData<Industry> = MutableLiveData()
    fun getIndustryState(): LiveData<Industry> = _industryState

    private val _salaryState: MutableLiveData<Long> = MutableLiveData()
    fun getSalaryState(): LiveData<Long> = _salaryState

    private val _onlyWithSalaryState: MutableLiveData<Boolean> = MutableLiveData()
    fun getOnlyWithSalaryState(): LiveData<Boolean> = _onlyWithSalaryState

    private val _applyButtonState: MutableLiveData<Boolean> = MutableLiveData()
    fun getApplyButtonState(): LiveData<Boolean> = _applyButtonState

    private val _resetButtonState: MutableLiveData<Boolean> = MutableLiveData()
    fun getResetButtonState(): LiveData<Boolean> = _resetButtonState

    init {
        readSavedSettings()
    }

    fun saveSalary(salary: Long) {
        settingsInteractor.write(WriteRequest.WriteSalary(salary), NEW_SETTINGS_KEY)
        setButtonState()
    }

    fun saveOnlyWithSalary(onlyWithSalary: Boolean) {
        settingsInteractor.write(WriteRequest.WriteOnlyWithSalary(onlyWithSalary), NEW_SETTINGS_KEY)
        _onlyWithSalaryState.postValue(onlyWithSalary)
        setButtonState()
    }

    private fun setButtonState() {
        val filterOn = getFilterOn()
        settingsInteractor.write(WriteRequest.WriteFilterOn(filterOn), NEW_SETTINGS_KEY)
        _resetButtonState.postValue(filterOn)
        _applyButtonState.postValue(compareValueSettings(settingsInteractor.read(NEW_SETTINGS_KEY)))
    }

//    fun setIsRequest(isRequest: Boolean) {
//        settingsInteractor.write(WriteRequest.WriteIsRequest(isRequest))
//    }

    private fun readSavedSettings() {
        val savedFilterSettings = settingsInteractor.read(OLD_SETTINGS_KEY)
        savedCountry = savedFilterSettings.country
        savedArea = savedFilterSettings.area
        savedIndustry = savedFilterSettings.industry
        savedSalary = savedFilterSettings.salary
        savedOnlyWithSalary = savedFilterSettings.onlyWithSalary
    }

    fun readNewSettings() {
        val newFilterSettings = settingsInteractor.read(NEW_SETTINGS_KEY)
        _placeWorkState.postValue(formatterPlaceWork(newFilterSettings))
        _industryState.postValue(newFilterSettings.industry)
        _onlyWithSalaryState.postValue(newFilterSettings.onlyWithSalary)
        _salaryState.postValue(newFilterSettings.salary)
        _applyButtonState.postValue(compareValueSettings(newFilterSettings))
        _resetButtonState.postValue(isSettingsEmpty(newFilterSettings))
    }

    private fun compareValueSettings(newSettings: Settings): Boolean {
        return savedCountry.id != newSettings.country.id ||
            savedArea.id != newSettings.area.id ||
            savedIndustry.id != newSettings.industry.id ||
            savedSalary != newSettings.salary ||
            savedOnlyWithSalary != newSettings.onlyWithSalary
    }

    fun resetSettings() {
        settingsInteractor.clear(NEW_SETTINGS_KEY)
    }

    fun clearPlaceWork() {
        with(settingsInteractor) {
            write(WriteRequest.WriteCountry(EMPTY_COUNTRY), NEW_SETTINGS_KEY)
            write(WriteRequest.WriteArea(EMPTY_AREA), NEW_SETTINGS_KEY)
            _placeWorkState.postValue(formatterPlaceWork(read(NEW_SETTINGS_KEY)))
        }
        setButtonState()
    }

    fun clearIndustry() {
        settingsInteractor.write(WriteRequest.WriteIndustry(EMPTY_INDUSTRY), NEW_SETTINGS_KEY)
        _industryState.postValue(EMPTY_INDUSTRY)
        setButtonState()
    }

    private fun getFilterOn(): Boolean {
        val filterSettings = settingsInteractor.read(NEW_SETTINGS_KEY)
        return isSettingsEmpty(filterSettings)
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
        const val OLD_SETTINGS_KEY = "old settings"
        const val NEW_SETTINGS_KEY = "new settings"
    }
}
