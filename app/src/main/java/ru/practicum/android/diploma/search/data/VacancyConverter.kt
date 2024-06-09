package ru.practicum.android.diploma.search.data

import ru.practicum.android.diploma.search.data.dto.VacanciesResponse
import ru.practicum.android.diploma.search.domain.models.Area
import ru.practicum.android.diploma.search.domain.models.Item
import ru.practicum.android.diploma.search.domain.models.Vacancies
import ru.practicum.android.diploma.vacancy.domain.models.Employer
import ru.practicum.android.diploma.vacancy.domain.models.Salary

class VacancyConverter {
    fun map(response: VacanciesResponse): Vacancies {
        return Vacancies(
            items = response.items.map { item ->
                Item(
                    id = item.id,
                    name = item.name,
                    area = Area(item.area.id, item.area.name),
                    employer = Employer(item.employer.id, item.employer.name, item.employer.logoUrls),
                    salary = if (item.salary != null) {
                        Salary(
                            item.salary.currency,
                            item.salary.from,
                            item.salary.to
                        )
                    } else {
                        null
                    }
                )
            },
            found = response.found,
            page = response.page,
            pages = response.pages,
            perPage = response.perPage
        )
    }
}
