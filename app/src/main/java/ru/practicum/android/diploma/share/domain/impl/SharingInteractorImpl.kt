package ru.practicum.android.diploma.share.domain.impl

import ru.practicum.android.diploma.share.data.ExternalNavigator
import ru.practicum.android.diploma.share.domain.api.SharingInteractor
import ru.practicum.android.diploma.share.domain.models.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
) : SharingInteractor {
    override fun shareVacancy(link: String) {
        externalNavigator.shareLink(link)
    }

    override fun sendEmail(emailData: EmailData) {
        externalNavigator.sendEmail(emailData)
    }

    override fun callTo(number: String) {
        externalNavigator.callTo(number)
    }
}
