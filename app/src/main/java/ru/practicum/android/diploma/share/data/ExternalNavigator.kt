package ru.practicum.android.diploma.share.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import ru.practicum.android.diploma.share.domain.models.EmailData

class ExternalNavigator(
    private val context: Context
) {
    fun shareLink(link: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, link)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun sendEmail(emailData: EmailData) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailData.extraMail))
        intent.putExtra(Intent.EXTRA_SUBJECT, emailData.extraSubject)
        intent.putExtra(Intent.EXTRA_TEXT, emailData.extraText)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun callTo(number: String) {
        val intent = Intent(
            Intent.ACTION_DIAL,
            Uri.fromParts("tel", number, null)
        )
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}
