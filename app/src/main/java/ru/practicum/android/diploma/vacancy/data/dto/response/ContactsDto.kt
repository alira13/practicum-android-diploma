package ru.practicum.android.diploma.vacancy.data.dto.response

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
