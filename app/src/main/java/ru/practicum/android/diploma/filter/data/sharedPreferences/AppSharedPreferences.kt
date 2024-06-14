package ru.practicum.android.diploma.filter.data.sharedPreferences

import ru.practicum.android.diploma.filter.data.dto.FilterDto

interface AppSharedPreferences {
    fun saveFilter(filterDto: FilterDto)
    fun getFilter(): FilterDto
}
