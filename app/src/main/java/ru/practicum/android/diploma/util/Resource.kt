package ru.practicum.android.diploma.util

import ru.practicum.android.diploma.search.domain.models.Errors

sealed class Resource<T>(val data: T? = null, val error: Errors? = null) {
    class Success<T>(data: T): Resource<T>(data)
    class Error<T>(error: Errors, data: T? = null): Resource<T>(data, error)
}
