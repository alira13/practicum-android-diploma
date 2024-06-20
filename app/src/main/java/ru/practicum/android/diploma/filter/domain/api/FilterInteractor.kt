package ru.practicum.android.diploma.filter.domain.api

import ru.practicum.android.diploma.filter.domain.models.FilterResult

interface FilterInteractor {
    suspend fun getRegions(): FilterResult
    suspend fun getIndustries(): FilterResult
}
