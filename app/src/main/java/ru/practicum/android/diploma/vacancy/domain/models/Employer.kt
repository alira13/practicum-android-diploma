package ru.practicum.android.diploma.vacancy.domain.models

import ru.practicum.android.diploma.search.data.dto.reponse.LogoUrls

data class Employer(
    val id: String,
    val name: String,
    val logoUrls: LogoUrls?
)
