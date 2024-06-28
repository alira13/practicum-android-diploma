package ru.practicum.android.diploma.filter.domain.api

import ru.practicum.android.diploma.filter.domain.models.FilterResult

interface FilterRepository {
    suspend fun getRegions(): FilterResult
    suspend fun getIndustries(): FilterResult
}
