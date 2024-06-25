package ru.practicum.android.diploma.vacancy.ui

import android.content.Context
import android.text.Html
import android.widget.ImageView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetails

class DetailsShower(
    private val binding: FragmentVacancyBinding,
    private val context: Context
) {
    fun showContent(details: VacancyDetails) {
        binding.apply {
            progressBar.isVisible = false
            scrollViewContent.isVisible = true
            vacancyNameTv.text = details.name
            salaryTv.text = details.salary
            employerNameTv.text = details.employer ?: ""
            areaNameTv.text = details.area
            provideLogo(employerLogoIv, details.logoUrls)
            experienceTv.text = details.experience
            employmentTv.text = details.employment
            vacancyDescriptionTv.text = Html.fromHtml(details.description, Html.FROM_HTML_MODE_COMPACT)
            showKeySkills(details)
            showContacts(details)
        }
    }

    private fun showKeySkills(details: VacancyDetails) {
        binding.apply {
            if (details.keySkills?.isNotEmpty() == true) {
                keySkillsTextTv.text = details.keySkills
            } else {
                keySkillsTitleTv.isVisible = false
            }
        }
    }

    private fun showContacts(details: VacancyDetails) {
        binding.apply {
            val name = details.contactName
            val email = details.email
            val phones = details.phone
            showNamesAndEmails(details)
            showPhones(details)
            if (name == null && email == null && phones.isNullOrEmpty()) {
                contactsTitleTv.isVisible = false
                commentTitleTv.isVisible = false
                contactsTitleTv.isVisible = false
                contactPersonTitleTv.isVisible = false
                contactPersonTextTv.isVisible = false
                emailTextTv.isVisible = false
                emailTitleTv.isVisible = false
                phoneTextTv.isVisible = false
                phoneTitleTv.isVisible = false
                commentTitleTv.isVisible = false
                commentTextTv.isVisible = false
            }
        }
    }

    private fun showNamesAndEmails(details: VacancyDetails) {
        binding.apply {
            val name = details.contactName
            val email = details.email
            if (name != null) {
                contactPersonTextTv.text = name
            } else {
                contactPersonTitleTv.isVisible = false
                contactPersonTextTv.isVisible = false
            }
            if (email != null) {
                emailTextTv.text = email
            } else {
                emailTextTv.isVisible = false
                emailTitleTv.isVisible = false
            }
        }
    }

    private fun showPhones(details: VacancyDetails) {
        binding.apply {
            val phone = details.phone
            if (!phone.isNullOrEmpty()) {
                phoneTextTv.text = phone
                val comment = details.comment
                if (!comment.isNullOrEmpty()) {
                    commentTextTv.text = comment
                } else {
                    commentTextTv.isVisible = false
                }
            } else {
                phoneTextTv.isVisible = false
                phoneTitleTv.isVisible = false
            }
        }
    }

    private fun provideLogo(imageView: ImageView, url: String?) {
        Glide.with(context)
            .load(url)
            .placeholder(R.drawable.ic_placeholder)
            .transform(RoundedCorners(VacancyFragment.CORNER_RADIUS))
            .into(imageView)
    }
}
