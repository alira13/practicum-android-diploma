package ru.practicum.android.diploma.vacancy.data

import android.content.Context
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.search.data.dto.reponse.SalaryDto
import ru.practicum.android.diploma.util.currencyUTF
import ru.practicum.android.diploma.util.formatter
import ru.practicum.android.diploma.vacancy.data.dto.response.VacancyDetailsResponse
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetails

class DetailsConverter(
    private val context: Context
) {
    fun map(response: VacancyDetailsResponse): VacancyDetails {
        return VacancyDetails(
            id = response.id,
            name = response.name,
            employer = response.employer?.name,
            logoUrls = response.employer?.logoUrls?.px240,
            alternateUrl = response.alternateUrl,
            area = getArea(response),
            experience = response.experience?.name,
            salary = getSalary(response),
            employment = response.employment?.name,
            description = response.description,
            keySkills = getKeySkills(response),
            contactName = response.contacts?.name,
            phone = getPhone(response),
            email = response.contacts?.email,
            comment = getComment(response)
        )
    }

    private fun getArea(response: VacancyDetailsResponse): String {
        val address = response.address
        return if (address?.city != null) {
            context.getString(R.string.vacancy_address_text, address.city, address.street, address.building)
        } else {
            response.area.name
        }
    }

    private fun getSalary(response: VacancyDetailsResponse): String {
        val salary = response.salary
        return if (salary?.from != null && salary.to != null) {
            salaryFromAndToText(salary)
        } else if (salary?.from != null) {
            salaryFromText(salary)
        } else if (salary?.to != null) {
             salaryToText(salary)
        } else {
            context.getString(R.string.vacancy_salary_not_specified_text)
        }
    }
    private fun salaryFromAndToText(salary: SalaryDto): String {
        val from = salary.from?.let { formatter(it) }
        val to = salary.to?.let { formatter(it) }
        return context.getString(
            R.string.vacancy_salary_text_full,
            from,
            to,
            currencyUTF(salary.currency)
        )
    }

    private fun salaryFromText(salary: SalaryDto): String {
        val from = salary.from?.let { formatter(it) }
        return context.getString(
            R.string.vacancy_salary_text_from,
            from,
            currencyUTF(salary.currency)
        )
    }

    private fun salaryToText(salary: SalaryDto): String {
        val to = salary.to?.let { formatter(it) }
        return context.getString(
            R.string.vacansy_salary_text_to,
            to,
            currencyUTF(salary.currency)
        )
    }

    private fun getKeySkills(response: VacancyDetailsResponse): String {
        val keySkills = response.keySkills
        var skillsString = ""
        keySkills.forEach { skill ->
            skillsString += "\u00B7 ${skill.name}\n"
        }
        return skillsString
    }

    private fun getPhone(response: VacancyDetailsResponse): String? {
        return response.contacts?.phones?.firstOrNull()?.number
    }

    private fun getComment(response: VacancyDetailsResponse): String? {
        return response.contacts?.phones?.firstOrNull()?.comment
    }


}
