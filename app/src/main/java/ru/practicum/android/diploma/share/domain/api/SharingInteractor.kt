package ru.practicum.android.diploma.share.domain.api

import ru.practicum.android.diploma.share.domain.models.EmailData

interface SharingInteractor {
    fun shareVacancy(link: String)
    fun sendEmail(emailData: EmailData)
    fun callTo(number: String)
}
