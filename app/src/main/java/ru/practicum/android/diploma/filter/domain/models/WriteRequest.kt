package ru.practicum.android.diploma.filter.domain.models

sealed interface WriteRequest {
    data class WriteRegion(
        val region: Region
    ) : WriteRequest

    data class WriteIndustry(
        val industry: Industry
    ) : WriteRequest

    data class WriteSalary(
        val salary: Int
    ) : WriteRequest
}
