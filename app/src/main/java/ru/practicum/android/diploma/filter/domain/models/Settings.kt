package ru.practicum.android.diploma.filter.domain.models

data class Settings(
    val industry: Industry,
    val country: Country,
    val area: Area,
    val salary: Long,
    val onlyWithSalary: Boolean,
    val filterOn: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Settings) return false
        return (
            industry.id == other.industry.id
                && country.id == other.country.id
                && area.id == other.area.id
                && salary == other.salary
                && onlyWithSalary == other.onlyWithSalary
                && filterOn == other.filterOn
            )
    }
}
