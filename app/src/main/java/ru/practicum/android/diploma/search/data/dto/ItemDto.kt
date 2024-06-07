package ru.practicum.android.diploma.search.data.dto

import com.google.gson.annotations.SerializedName

data class ItemDto(
    @SerializedName("accept_incomplete_resumes")
    val acceptIncompleteResumes: Boolean,
    @SerializedName("accept_temporary")
    val acceptTemporary: Boolean?,
    val address: Address,
    @SerializedName("alternate_url")
    val alternateUrl: String,
    @SerializedName("apply_alternate_url")
    val applyAlternateUrl: String,
    val archived: Boolean?,
    val area: Area,
    val contacts: Contacts,
    @SerializedName("created_at")
    val createdAt: String,
    val department: Department?,
    val employer: Employer,
    @SerializedName("has_test")
    val hasTest: Boolean,
    val id: String,
    @SerializedName("insider_interview")
    val insiderInterview: InsiderInterview?,
    @SerializedName("metro_stations")
    val metroStations: List<MetroStation>,
    val name: String,
    val premium: Boolean?,
    @SerializedName("professional_roles")
    val professionalRoles: List<ProfessionalRole>,
    @SerializedName("published_at")
    val publishedAt: String,
    val relations: Array<String>?,
    @SerializedName("response_letter_required")
    val responseLetterRequired: Boolean,
    @SerializedName("response_url")
    val responseUrl: String?,
    val salary: Salary,
    val schedule: Schedule,
    val type: Type,
    val url: String,
    val experience: Experience,
    val snippet: Snippet,
    @SerializedName("show_logo_in_search")
    val showLogoInSearch: Boolean?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ItemDto

        if (acceptIncompleteResumes != other.acceptIncompleteResumes) return false
        if (acceptTemporary != other.acceptTemporary) return false
        if (address != other.address) return false
        if (alternateUrl != other.alternateUrl) return false
        if (applyAlternateUrl != other.applyAlternateUrl) return false
        if (archived != other.archived) return false
        if (area != other.area) return false
        if (contacts != other.contacts) return false
        if (createdAt != other.createdAt) return false
        if (department != other.department) return false
        if (employer != other.employer) return false
        if (hasTest != other.hasTest) return false
        if (id != other.id) return false
        if (insiderInterview != other.insiderInterview) return false
        if (metroStations != other.metroStations) return false
        if (name != other.name) return false
        if (premium != other.premium) return false
        if (professionalRoles != other.professionalRoles) return false
        if (publishedAt != other.publishedAt) return false
        if (!relations.contentEquals(other.relations)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = acceptIncompleteResumes.hashCode()
        result = 31 * result + (acceptTemporary?.hashCode() ?: 0)
        result = 31 * result + address.hashCode()
        result = 31 * result + alternateUrl.hashCode()
        result = 31 * result + applyAlternateUrl.hashCode()
        result = 31 * result + (archived?.hashCode() ?: 0)
        result = 31 * result + area.hashCode()
        result = 31 * result + contacts.hashCode()
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + (department?.hashCode() ?: 0)
        result = 31 * result + employer.hashCode()
        result = 31 * result + hasTest.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + (insiderInterview?.hashCode() ?: 0)
        result = 31 * result + metroStations.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + (premium?.hashCode() ?: 0)
        result = 31 * result + professionalRoles.hashCode()
        result = 31 * result + publishedAt.hashCode()
        result = 31 * result + relations.contentHashCode()
        return result
    }
}

data class Schedule(
    val id: String?,
    val name: String
)

data class Type(
    val id: String,
    val name: String
)

data class Experience(
    val id: String,
    val name: String
)

data class Snippet(
    val requirement: String?,
    val responsibility: String?
)
data class Address(
    val building: String?,
    val city: String?,
    val description: String?,
    val lat: Double?,
    val lng: Double?,
    @SerializedName("metro_stations")
    val metroStations: List<MetroStation>,
    val raw: String?,
    val street: String?
)

data class MetroStation(
    val lat: Double?,
    @SerializedName("line_id")
    val lineId: String,
    @SerializedName("line_name")
    val lineName: String,
    val lng: Double?,
    @SerializedName("station_id")
    val stationId: String,
    @SerializedName("station_name")
    val stationName: String
)

data class Area(
    val id: String,
    val name: String,
    val url: String
)

data class Contacts(
    val email: String?,
    val name: String?,
    val phones: List<Phone>?
)

data class Phone(
    val city: String,
    val comment: Any,
    val country: String,
    val formatted: String,
    val number: String
)

data class Department(
    val id: String,
    val name: String
)

data class Employer(
    @SerializedName("accredited_it_employer")
    val accreditedItEmployer: Boolean,
    @SerializedName("alternate_url")
    val alternateUrl: String,
    val id: String,
    @SerializedName("logo_urls")
    val logoUrls: LogoUrls,
    val name: String,
    val trusted: Boolean,
    val url: String
)

data class LogoUrls(
    @SerializedName("240")
    val px240: String,
    @SerializedName("90")
    val px90: String,
    val original: String
)

data class InsiderInterview(
    val id: String,
    val url: String
)

data class ProfessionalRole(
    val id: String,
    val name: String
)

data class Salary(
    val currency: String,
    val from: Int,
    val gross: Boolean,
    val to: Any
)
