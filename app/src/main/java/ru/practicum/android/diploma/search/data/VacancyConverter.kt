package ru.practicum.android.diploma.search.data

import android.content.Context
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.search.data.dto.reponse.SalaryDto
import ru.practicum.android.diploma.search.data.dto.reponse.VacanciesResponse
import ru.practicum.android.diploma.search.domain.models.SearchResult
import ru.practicum.android.diploma.search.domain.models.VacancyPreview

class VacancyConverter(
    private val context: Context
) {
    fun map(response: VacanciesResponse): SearchResult.SearchContent {
        return SearchResult.SearchContent(
            vacancies = response.items.map { item ->
                VacancyPreview(
                    id = item.id,
                    iconUrl = if (item.employer.logoUrls != null) {
                        item.employer.logoUrls.px90
                    } else {
                        null
                    },
                    description = item.name.plus(", ${item.area.name}"),
                    employer = item.employer.name,
                    salary = parseSalary(item.salary)
                )
            },
            count = response.found,
            page = response.page,
            pages = response.pages
        )
    }

    private fun parseSalary(salary: SalaryDto?): String {
        if (salary != null) {
            return if (salary.to == null) {
                String.format(
                    context.getString(R.string.salary_from),
                    salary.from,
                    currencyEnd(salary.currency)
                )
            } else {
                String.format(
                    context.getString(R.string.salary_from_to),
                    salary.from,
                    salary.to,
                    currencyEnd(salary.currency)
                )
            }
        } else {
            return context.getString(R.string.salary_is_null)
        }
    }

    private fun currencyEnd(currency: String): String {
        return when (currency) {
            RUR -> "₽"
            USD -> "$"
            EUR -> "€"
            else -> currency
        }
    }

    companion object {
        private const val RUR = "RUR"
        private const val USD = "USD"
        private const val EUR = "EUR"
    }
}
