package ru.practicum.android.diploma.search.data

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.search.data.dto.reponse.SalaryDto
import ru.practicum.android.diploma.search.data.dto.reponse.VacanciesResponse
import ru.practicum.android.diploma.search.domain.models.SearchResult
import ru.practicum.android.diploma.search.domain.models.VacancyPreview
import ru.practicum.android.diploma.util.currencyUTF
import ru.practicum.android.diploma.util.formatter
import java.util.Locale

class VacancyConverter(
    private val context: Context
) {
    fun map(response: VacanciesResponse): SearchResult.SearchContent {
        return SearchResult.SearchContent(
            vacancies = response.items.map { item ->
                VacancyPreview(
                    id = item.id,
                    iconUrl = if (item.employer.logoUrls != null) {
                        item.employer.logoUrls.px240
                    } else {
                        null
                    },
                    description = item.name.plus(", ${item.area.name}"),
                    employer = item.employer.name,
                    salary = parseSalary(item.salary)
                )
            },
            count = convertToPlurals(response.found),
            page = response.page,
            pages = response.pages
        )
    }

    private fun parseSalary(salary: SalaryDto?): String {
        if (salary != null) {
            return if (salary.to?.toInt() == 0 || salary.to == null) {
                String.format(
                    context.getString(R.string.salary_from),
                    salary.from?.let { formatter(salary.from) },
                    currencyUTF(salary.currency)
                )

            } else if (salary.from?.toInt() == 0 || salary.from == null) {
                String.format(
                    context.getString(R.string.salary_to),
                    formatter(salary.to),
                    currencyUTF(salary.currency)
                )

            } else {
                String.format(
                    context.getString(R.string.salary_from_to),
                    formatter(salary.from),
                    formatter(salary.to),
                    currencyUTF(salary.currency)
                )
            }
        } else {
            return context.getString(R.string.salary_is_null)
        }
    }

    private fun convertToPlurals(count: Int): String {
        val languageTag = "ru"
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.create(Locale.forLanguageTag(languageTag))
        )
        return context.resources.getQuantityString(R.plurals.vacancies_amount, count, count)
    }
}
