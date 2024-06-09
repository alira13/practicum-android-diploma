package ru.practicum.android.diploma.vacancy.data.dto

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.search.data.dto.AreaDto
import ru.practicum.android.diploma.search.data.dto.EmployerDto
import ru.practicum.android.diploma.search.data.dto.Response
import ru.practicum.android.diploma.search.data.dto.SalaryDto

data class VacancyDetailsResponse(
    val id: String,
    val name: String,
    val employer: EmployerDto?,
    @SerializedName("alternate_url")
    val alternateUrl: String,
    val area: AreaDto,
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

data class ScheduleDto(
    val id: String?,
    val name: String
)

data class ExperienceDto(
    val id: String,
    val name: String
)

data class EmploymentDto(
    val id: String?,
    val name: String
)

data class RoleDto(
    val id: String,
    val name: String
)

data class SkillDto(
    val name: String
)

data class ContactsDto(
    val email: String?,
    val name: String?,
    val phones: Array<PhoneDto>?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (javaClass != other?.javaClass) {
            return false
        }

        other as ContactsDto

        if (email != other.email) {
            return false
        }
        if (name != other.name) {
            return false
        }
        if (phones != null) {
            if (other.phones == null) {
                return false
            }
            if (!phones.contentEquals(other.phones)) {
                return false
            }
        } else if (other.phones != null) {
            return false
        }

        return true
    }

    override fun hashCode(): Int {
        var result = email?.hashCode() ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (phones?.contentHashCode() ?: 0)
        return result
    }
}

data class PhoneDto(
    val city: String,
    val comment: String?,
    val country: String,
    val formatted: String,
    val number: String
)
