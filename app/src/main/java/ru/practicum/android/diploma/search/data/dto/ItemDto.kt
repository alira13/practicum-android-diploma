package ru.practicum.android.diploma.search.data.dto

import com.google.gson.annotations.SerializedName

data class ItemDto(
    val id: String,
    val name: String,
    val area: AreaDto,
    val employer: EmployerDto,
    val salary: SalaryDto?,
)

data class AreaDto(
    val id: String,
    val name: String,
)
data class EmployerDto(
    val id: String,
    val name: String,
    @SerializedName("logo_urls")
    val logoUrls: LogoUrls?
)
data class SalaryDto(
    val currency: String,
    val from: Long,
    val to: Long
)

data class LogoUrls(
    @SerializedName("240")
    val px240: String,
    @SerializedName("90")
    val px90: String,
    val original: String
)

