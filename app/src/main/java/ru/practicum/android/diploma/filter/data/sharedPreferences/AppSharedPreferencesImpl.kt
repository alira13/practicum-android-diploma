package ru.practicum.android.diploma.filter.data.sharedPreferences

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.practicum.android.diploma.filter.data.dto.FilterDto

class AppSharedPreferencesImpl(
    private val sharedPreferences: SharedPreferences,
    private val json: Gson
) : AppSharedPreferences {
    override fun saveFilter(filterDto: FilterDto) {
        val jsonString = json.toJson(filterDto)
        sharedPreferences.edit()
            .putString(FILTER, jsonString)
            .apply()
    }

    override fun getFilter(): FilterDto {
        val jsonString = sharedPreferences.getString(FILTER, null) ?: return FilterDto()
        val token = object : TypeToken<FilterDto>() {}.type
        return json.fromJson(jsonString, token)
    }

    companion object {
        const val FILTER = "filter"
    }
}
