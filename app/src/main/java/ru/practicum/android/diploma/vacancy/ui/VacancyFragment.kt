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
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.search.domain.models.Errors
import ru.practicum.android.diploma.share.domain.models.EmailData
import ru.practicum.android.diploma.util.BindingFragment
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetails
import ru.practicum.android.diploma.vacancy.presentation.VacancyDetailsViewModel
import ru.practicum.android.diploma.vacancy.ui.models.VacancyDetailsUIState

class VacancyFragment : BindingFragment<FragmentVacancyBinding>() {

    private val viewModel: VacancyDetailsViewModel by viewModel {
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
    }

    private fun setClickListeners(details: VacancyDetails) {
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
            favoriteIc.setOnClickListener {
                it.isPressed != it.isPressed
                viewModel.changeFavoriteState(details)
            }
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
                setClickListeners(state.details)
            }
        }
    }

    private fun showContent(details: VacancyDetails) {
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
            favoriteIc.isPressed = details.isFavorite
            vacancyDescriptionTv.text = Html.fromHtml(details.description, Html.FROM_HTML_MODE_COMPACT)
            showKeySkills(details)
            showContacts(details)
        }
        vacancyUrl = details.alternateUrl

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
