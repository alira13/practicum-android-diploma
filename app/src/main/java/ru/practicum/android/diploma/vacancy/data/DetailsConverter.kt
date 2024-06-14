package ru.practicum.android.diploma.vacancy.data

import android.content.Context
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.vacancy.data.dto.response.AddressDto
import ru.practicum.android.diploma.vacancy.data.dto.response.SkillDto
import ru.practicum.android.diploma.vacancy.data.dto.response.VacancyDetailsResponse
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetailsR

class DetailsConverter(
    private val context: Context
) {
    fun map(response: VacancyDetailsResponse): VacancyDetailsR {
        return VacancyDetailsR(
            id = response.id,
            name = response.name,
            alternateUrl = response.alternateUrl,
            currency = response.salary?.currency,
            salaryFrom = response.salary?.from,
            salaryTo = response.salary?.to,
            employerName = response.employer?.name,
            employerLogo = response.employer?.logoUrls?.px90,
            areaName = response.area.name,
            address = getAddress(response.address),
            experience = response.experience?.name,
            employment = response.employment?.name,
            schedule = response.schedule.name,
            description = response.description,
            keySkills = getKeySkills(response.keySkills),
            contactName = response.contacts?.name,
            contactEmail = response.contacts?.email,
            contactPhone = response.contacts?.phones?.firstOrNull()?.number,
            comment = response.contacts?.phones?.firstOrNull()?.comment
        )
    }

    private fun getAddress(address: AddressDto?): String? {
       return address?.let {
           context.getString(R.string.vacancy_address_text, address.city, address.street, address.building)
       }
    }

    private fun getKeySkills(keySkills: Array<SkillDto>): String {
        var skillsString = ""
        keySkills.forEach { skill ->
            skillsString += "\u00B7 ${skill.name}\n"
        }
        return skillsString
    }
}
