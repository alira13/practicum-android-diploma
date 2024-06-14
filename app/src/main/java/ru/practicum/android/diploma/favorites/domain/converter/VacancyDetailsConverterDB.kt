package ru.practicum.android.diploma.favorites.domain.converter

import ru.practicum.android.diploma.favorites.domain.models.VacancyDetailsDB
import ru.practicum.android.diploma.vacancy.domain.models.Phone
import ru.practicum.android.diploma.vacancy.domain.models.Skill
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetails

class VacancyDetailsConverterDB {

    fun convertToVacancyDetailsDB(vacancy: VacancyDetails): VacancyDetailsDB = VacancyDetailsDB(
        id = vacancy.id,
        name = vacancy.name,
        currency = vacancy.salary?.currency,
        salaryFrom = vacancy.salary?.from,
        salaryTo = vacancy.salary?.to,
        employerName = vacancy.employment?.name,
        employerLogo = vacancy.employer?.logoUrls?.px240,
        areaName = vacancy.area.name,
        experience = vacancy.experience?.name,
        employment = vacancy.employment?.name,
        schedule = vacancy.schedule.name,
        description = vacancy.description,
        keySkills = vacancy.keySkills.map { parseSkills(it) },
        contactName = vacancy.contacts?.name,
        contactEmail = vacancy.contacts?.email,
        contactPhone = vacancy.contacts?.phones?.map { parseNumber(it) },
        comment = vacancy.contacts?.phones?.map { parseComment(it) }
    )

    private fun parseNumber(phone: Phone): String {
        return "+${phone.country} (${phone.city}) ${phone.number}"
    }

    private fun parseComment(phone: Phone): String? {
        return phone.comment
    }

    private fun parseSkills(skill: Skill): String {
        return skill.name
    }

}
