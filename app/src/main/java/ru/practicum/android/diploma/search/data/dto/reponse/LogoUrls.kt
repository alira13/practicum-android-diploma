package ru.practicum.android.diploma.search.data.dto.reponse

import com.google.gson.annotations.SerializedName

data class LogoUrls(
    @SerializedName("240")
    val px240: String,
    @SerializedName("90")
    val px90: String,
    val original: String
)
