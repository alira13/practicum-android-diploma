package ru.practicum.android.diploma.search.domain.api

import ru.practicum.android.diploma.search.domain.models.Errors
import ru.practicum.android.diploma.search.domain.models.Request
import ru.practicum.android.diploma.search.domain.models.Vacancies

interface SearchInteractor {
    suspend fun searchVacancies(request: Request): Pair<Vacancies?, Errors?>
}
