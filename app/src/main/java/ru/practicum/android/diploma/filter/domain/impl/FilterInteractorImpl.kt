package ru.practicum.android.diploma.filter.domain.impl

import ru.practicum.android.diploma.filter.domain.api.FilterInteractor
import ru.practicum.android.diploma.filter.domain.api.FilterRepository
import ru.practicum.android.diploma.filter.domain.models.FilterResult

class FilterInteractorImpl(
    private val filterRepository: FilterRepository,
) : FilterInteractor {
    override suspend fun getRegions(): FilterResult {
        return filterRepository.getRegions()
    }

    override suspend fun getIndustries(): FilterResult {
        return filterRepository.getIndustries()
    }
}
