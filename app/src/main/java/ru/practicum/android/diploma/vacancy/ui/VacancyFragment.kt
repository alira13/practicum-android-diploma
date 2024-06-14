package ru.practicum.android.diploma.vacancy.ui

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.search.domain.models.Errors
import ru.practicum.android.diploma.share.domain.models.EmailData
import ru.practicum.android.diploma.util.BindingFragment
import ru.practicum.android.diploma.util.currencyUTF
import ru.practicum.android.diploma.util.formatter
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetailsR
import ru.practicum.android.diploma.vacancy.presentation.VacancyDetailsViewModel
import ru.practicum.android.diploma.vacancy.ui.models.VacancyDetailsUIState

class VacancyFragment : BindingFragment<FragmentVacancyBinding>() {

    private val viewModel by inject<VacancyDetailsViewModel> {
        parametersOf(vacancyID)
    }
    private var vacancyID: String? = null
    private var vacancyUrl: String = ""

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentVacancyBinding {
        return FragmentVacancyBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vacancyID = requireArguments().getString(VACANCY_ID)
        val toolbar = binding.vacancyToolbar
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        viewModel.getUIState().observe(viewLifecycleOwner) { state ->
            render(state)
        }
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.apply {
            emailTextTv.setOnClickListener {
                val email = emailTextTv.text.toString()
                Log.d("VacancyFragment", "mail to $email")
                val emailData = EmailData(
                    extraMail = arrayOf(email),
                    "",
                    ""
                )
                viewModel.sendEmail(emailData)
            }
            shareIc.setOnClickListener {
                if (vacancyUrl.isNotEmpty()) {
                    viewModel.shareVacancy(vacancyUrl)
                }
            }
            phoneTextTv.setOnClickListener {
                val phone = phoneTextTv.text.toString()
                viewModel.callTo(phone)
            }
            favoriteIc.setOnClickListener { }
        }
    }

    private fun render(state: VacancyDetailsUIState) {
        when (state) {
            is VacancyDetailsUIState.Loading -> {
                showLoading()
            }

            is VacancyDetailsUIState.Error -> {
                showError(state.errors)
            }

            is VacancyDetailsUIState.Content -> {
                showContent(state.details)
            }
        }
    }

    private fun showContent(details: VacancyDetailsR) {
        binding.apply {
            progressBar.isVisible = false
            scrollViewContent.isVisible = true
            vacancyNameTv.text = details.name
            showSalary(details)
            employerNameTv.text = details.employerName ?: ""
            showArea(details)
            val logoUrls = details.employerLogo
            provideLogo(employerLogoIv, logoUrls)
            showExperience(details)
            val employment = details.employment
            if (employment != null) {
                employmentTv.text = employment
            }
            val description = details.description
            vacancyDescriptionTv.text = Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT)
            showKeySkills(details.keySkills)
            showContacts(details)
        }
        vacancyUrl = details.alternateUrl
    }

    private fun showSalary(details: VacancyDetailsR) {
        binding.apply {
            val salaryFrom = details.salaryFrom
            val salaryTo = details.salaryTo
            val currency = details.currency
            if (salaryFrom != null && salaryTo != null) {
                salaryTv.text = salaryFromAndToText(salaryFrom, salaryTo, currency)
            } else if (salaryFrom != null) {
                salaryTv.text = salaryFromText(salaryFrom, currency)
            } else if (salaryTo != null) {
                salaryTv.text = salaryToText(salaryTo, currency)
            } else  {
                salaryTv.text = getString(R.string.vacancy_salary_not_specified_text)
            }
        }
    }

    private fun showArea(details: VacancyDetailsR) {
        binding.apply {
            val address = details.address
            if (address != null) {
                areaNameTv.text = address
            } else {
                areaNameTv.text = details.areaName
            }
        }
    }

    private fun showExperience(details: VacancyDetailsR) {
        binding.apply {
            val experience = details.experience
            if (experience != null) {
                experienceTv.text = experience
            }
        }
    }

    private fun showKeySkills(keySkills: String?) {
        binding.apply {
            if (keySkills?.isNotEmpty() == true) {
                keySkillsTextTv.text = keySkills
            } else {
                keySkillsTitleTv.isVisible = false
                keySkillsTitleTv.isVisible = false
            }
        }
    }

    private fun showContacts(details: VacancyDetailsR) {
        binding.apply {
            val name = details.contactName
            val email = details.contactEmail
            val phone = details.contactPhone
            if (name == null && email == null && phone.isNullOrEmpty()) {
                contactsTitleTv.isVisible = false
                contactPersonTitleTv.isVisible = false
                contactPersonTextTv.isVisible = false
                emailTextTv.isVisible = false
                emailTitleTv.isVisible = false
                phoneTextTv.isVisible = false
                phoneTitleTv.isVisible = false
                commentTitleTv.isVisible = false
                commentTextTv.isVisible = false
            } else {
                showNamesAndEmails(name, email)
                showPhones(details)
            }
        }
    }

    private fun showNamesAndEmails(name: String?, email: String?) {
        binding.apply {
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

    private fun showPhones(details: VacancyDetailsR) {
        binding.apply {
            val phone = details.contactPhone
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

    private fun showError(error: Errors?) {
        with(binding) {
            progressBar.isVisible = false
            vacancyErrorPlaceHolder.isVisible = true
            scrollViewContent.isVisible = false
            when (error) {
                is Errors.ServerError -> {
                    vacancyErrorPlaceHolderErrorText.text = getString(R.string.server_error_text)
                }

                is Errors.ConnectionError -> {
                    vacancyErrorPlaceHolderErrorText.text = getString(R.string.no_internet)
                }

                is Errors.IncorrectRequest -> {
                    vacancyErrorPlaceHolderErrorText.text = getString(R.string.incorrect_request_text)
                }

                else -> {}
            }
        }
    }

    private fun showLoading() {
        binding.apply {
            progressBar.isVisible = true
            vacancyErrorPlaceHolder.isVisible = false
            scrollViewContent.isVisible = false
        }
    }

    private fun salaryFromAndToText(salaryFrom: Long, salaryTo: Long, currency: String?): String {
        val from = formatter(salaryFrom)
        val to = formatter(salaryTo)
        return getString(
            R.string.vacancy_salary_text_full,
            from,
            to,
            currencyUTF(currency)
        )
    }

    private fun salaryFromText(salaryFrom: Long, currency: String?): String {
        val from =formatter(salaryFrom)
        return getString(
            R.string.vacancy_salary_text_from,
            from,
            currencyUTF(currency)
        )
    }

    private fun salaryToText(salaryTo: Long, currency: String?): String {
        val to = formatter(salaryTo)
        return getString(
            R.string.vacansy_salary_text_to,
            to,
            currencyUTF(currency)
        )
    }

    private fun provideLogo(imageView: ImageView, url: String?) {
        Glide.with(requireContext())
            .load(url)
            .placeholder(R.drawable.ic_placeholder)
            .transform(RoundedCorners(CORNER_RADIUS))
            .into(imageView)
    }

    companion object {
        private const val VACANCY_ID = "vacancy_id"
        const val CORNER_RADIUS = 12

        fun createArgs(vacancyID: String): Bundle =
            bundleOf(VACANCY_ID to vacancyID)
    }
}
