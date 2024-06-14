package ru.practicum.android.diploma.vacancy.data.dto.response

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.search.data.dto.reponse.AreaDto
import ru.practicum.android.diploma.search.data.dto.reponse.EmployerDto
import ru.practicum.android.diploma.search.data.dto.reponse.Response
import ru.practicum.android.diploma.search.data.dto.reponse.SalaryDto

data class VacancyDetailsResponse(
    val id: String,
    val name: String,
    val employer: EmployerDto?,
    @SerializedName("alternate_url")
    val alternateUrl: String,
    val area: AreaDto,
    val address: AddressDto?,
    val experience: ExperienceDto?,
    val salary: SalaryDto?,
    val schedule: ScheduleDto,
    val employment: EmploymentDto?,
    val description: String,
    @SerializedName("key_skills")
    val keySkills: Array<SkillDto>,
    @SerializedName("professional_roles")
    val professionalRoles: Array<RoleDto>,
    val contacts: ContactsDto?
) : Response() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VacancyDetailsResponse

        if (id != other.id) {
            return false
        }
        if (name != other.name) {
            return false
        }
        if (!keySkills.contentEquals(other.keySkills)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + keySkills.contentHashCode()
        return result
    }
}
