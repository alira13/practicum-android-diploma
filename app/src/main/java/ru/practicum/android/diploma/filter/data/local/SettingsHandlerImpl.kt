package ru.practicum.android.diploma.filter.data.local

import android.content.SharedPreferences
import com.google.gson.Gson
import ru.practicum.android.diploma.filter.data.api.SettingsHandler
import ru.practicum.android.diploma.filter.data.dto.CountryDto
import ru.practicum.android.diploma.filter.data.dto.IndustryDto
import ru.practicum.android.diploma.filter.data.dto.SettingsDto
import ru.practicum.android.diploma.search.data.dto.reponse.AreaDto

class SettingsHandlerImpl(
    private val preferences: SharedPreferences,
    private val gson: Gson
) : SettingsHandler {
    override fun read(settingsKey: String): SettingsDto {
        val json = preferences.getString(settingsKey, null)
        return if (json != null) {
            gson.fromJson(json, SettingsDto::class.java)
        } else {
            SettingsDto(
                industry = IndustryDto("", ""),
                country = CountryDto("", ""),
                salary = 0,
                area = AreaDto("", "", ""),
                onlyWithSalary = false,
                filterOn = false,
            )
        }
    }

    override fun write(settingsDto: SettingsDto, settingsKey: String): Boolean {
        val json = gson.toJson(settingsDto)
        preferences.edit()
            .putString(settingsKey, json)
            .apply()
        return true
    }

    override fun clear(settingsKey: String) {
        preferences.edit().remove(settingsKey).apply()
    }

//    companion object {
//        const val SETTINGS_KEY = "settings key"
//        const val APPLIED_SETTINGS_KEY = "applied settings key"
//    }
}
