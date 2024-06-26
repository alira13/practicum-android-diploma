package ru.practicum.android.diploma.filter.domain.models

sealed interface WriteRequest {
    data class WriteCountry(
        val country: Country,
    ) : WriteRequest

    data class WriteArea(
        val area: Area,
    ) : WriteRequest

    data class WriteIndustry(
        val industry: Industry,
    ) : WriteRequest

    data class WriteSalary(
        val salary: Long,
    ) : WriteRequest

    data class WriteOnlyWithSalary(
        val onlyWithSalary: Boolean,
    ) : WriteRequest

    data class WriteFilterOn(
        val filterOn: Boolean,
    ) : WriteRequest

    data class WriteIsRequest(
        val isRequest: Boolean
    ) : WriteRequest
}
