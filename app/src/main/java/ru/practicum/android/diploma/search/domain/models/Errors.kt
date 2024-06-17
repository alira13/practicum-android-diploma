package ru.practicum.android.diploma.search.domain.models

sealed class Errors {
    data object ConnectionError : Errors()
    data object IncorrectRequest : Errors()
    data object ServerError: Errors()
    data object Error404 : Errors()
}
