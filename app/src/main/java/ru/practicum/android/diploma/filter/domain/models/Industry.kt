package ru.practicum.android.diploma.filter.domain.models

import kotlinx.serialization.Serializable

@Serializable
class Industry(
    val id: String,
    val name: String,
    var isChecked: Boolean = false
)
