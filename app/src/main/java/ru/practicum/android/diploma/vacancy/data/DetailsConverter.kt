package ru.practicum.android.diploma.vacancy.data

import ru.practicum.android.diploma.search.domain.models.Area
import ru.practicum.android.diploma.vacancy.data.dto.response.VacancyDetailsResponse
import ru.practicum.android.diploma.vacancy.domain.models.Address
import ru.practicum.android.diploma.vacancy.domain.models.Contacts
import ru.practicum.android.diploma.vacancy.domain.models.Employer
import ru.practicum.android.diploma.vacancy.domain.models.Employment
import ru.practicum.android.diploma.vacancy.domain.models.Experience
import ru.practicum.android.diploma.vacancy.domain.models.Phone
import ru.practicum.android.diploma.vacancy.domain.models.Role
import ru.practicum.android.diploma.vacancy.domain.models.Salary
import ru.practicum.android.diploma.vacancy.domain.models.Schedule
import ru.practicum.android.diploma.vacancy.domain.models.Skill
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetails

class DetailsConverter {
    fun map(response: VacancyDetailsResponse): VacancyDetails {
        return VacancyDetails(
            id = response.id,
            name = response.name,
            employer = getEmployer(response),
            alternateUrl = response.alternateUrl,
            area = Area(response.area.id, response.area.name),
            address = Address(response.address?.building, response.address?.city, response.address?.street),
            experience = getExperience(response),
            salary = getSalary(response),
            schedule = Schedule(response.schedule.id, response.schedule.name),
            employment = getEmployment(response),
            description = response.description,
            keySkills = response.keySkills.map { Skill(it.name) },
            professionalRoles = response.professionalRoles.map { Role(it.id, it.name) },
            contacts = getContacts(response)
        )
    }

    private fun getEmployer(response: VacancyDetailsResponse): Employer? {
        return if (response.employer == null) {
            null
        } else {
            Employer(response.employer.id, response.employer.name, response.employer.logoUrls)
        }
    }

    private fun getExperience(response: VacancyDetailsResponse): Experience? {
        return if (response.experience == null) {
            null
        } else {
            Experience(response.experience.id, response.experience.name)
        }
    }

    private fun getSalary(response: VacancyDetailsResponse): Salary? {
        return if (response.salary == null) {
            null
        } else {
            Salary(response.salary.currency, response.salary.from, response.salary.to)
        }
    }

    private fun getEmployment(response: VacancyDetailsResponse): Employment? {
        return if (response.employment == null) {
            null
        } else {
            Employment(response.employment.id, response.employment.name)
        }
    }

    private fun getContacts(response: VacancyDetailsResponse): Contacts? {
        return if (response.contacts == null) {
            null
        } else {
            Contacts(
                email = response.contacts.email,
                name = response.contacts.name,
                phones = response.contacts.phones?.map {
                    Phone(
                        city = it.city,
                        comment = it.comment,
                        country = it.country,
                        formatted = it.formatted,
                        number = it.number
                    )
                }
            )
        }
    }
}
