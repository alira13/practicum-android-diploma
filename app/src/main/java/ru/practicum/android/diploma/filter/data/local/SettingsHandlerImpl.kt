package ru.practicum.android.diploma.filter.data.local

import android.content.SharedPreferences
import com.google.gson.Gson
import ru.practicum.android.diploma.filter.data.api.SettingsHandler
import ru.practicum.android.diploma.filter.data.dto.SettingsDto

class SettingsHandlerImpl(
    private val preferences: SharedPreferences,
    private val gson: Gson
) : SettingsHandler {
    override fun read(): SettingsDto {
        val json = preferences.getString(SETTINGS_KEY, null)
        return gson.fromJson(json, SettingsDto::class.java)
    }

    override fun write(settingsDto: SettingsDto): Boolean {
        val json = gson.toJson(settingsDto)
        preferences.edit()
            .putString(SETTINGS_KEY, json)
            .apply()
        return true
    }

    override fun clear() {
        preferences.edit().remove(SETTINGS_KEY).apply()
    }

    companion object {
        const val SETTINGS_KEY = "settings key"
    }
}
