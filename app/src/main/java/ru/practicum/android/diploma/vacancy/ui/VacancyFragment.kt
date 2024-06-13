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
import ru.practicum.android.diploma.util.BindingFragment
import ru.practicum.android.diploma.util.currencyUTF
import ru.practicum.android.diploma.util.formatter
import ru.practicum.android.diploma.vacancy.domain.models.Salary
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetails
import ru.practicum.android.diploma.vacancy.presentation.VacancyDetailsViewModel
import ru.practicum.android.diploma.vacancy.ui.models.VacancyDetailsUIState

class VacancyFragment : BindingFragment<FragmentVacancyBinding>() {

    private val viewModel by inject<VacancyDetailsViewModel> {
        parametersOf(vacancyID)
    }
    private var vacancyID: String? = null

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

    private fun showContent(details: VacancyDetails) {
        binding.apply {
            progressBar.isVisible = false
            scrollViewContent.isVisible = true
            vacancyNameTv.text = details.name
            val salary = details.salary
            if (salary != null) {
                if (salary.from != null && salary.to != null) {
                    salaryTv.text = salaryFromAndToText(salary)
                }
                if (salary.from != null && salary.to == null) {
                    salaryTv.text = salaryFromText(salary)
                }
                if (salary.from == null && salary.to != null) {
                    salaryTv.text = salaryToText(salary)
                }
            } else {
                salaryTv.text = getString(R.string.vacancy_salary_not_specified_text)
            }
            employerNameTv.text = details.employer?.name ?: ""
            val address = details.address
            if (address?.city != null) {
                areaNameTv.text =
                    getString(R.string.vacancy_address_text, address.city, address.street, address.building)
            } else {
                areaNameTv.text = details.area.name
            }
            val logoUrls = details.employer?.logoUrls
            provideLogo(employerLogoIv, logoUrls?.px240)
            val experience = details.experience
            if (experience?.name != null) {
                experienceTv.text = experience.name
            }
            val employment = details.employment
            if (employment != null) {
                employmentTv.text = employment.name
            }
            val description = details.description
            vacancyDescriptionTv.text = Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT)
            val keySkills = details.keySkills
            if (keySkills.isNotEmpty()) {
                var skillsString = ""
                keySkills.forEach { skill ->
                    skillsString += "\u00B7 ${skill.name}\n"
                }
                keySkillsTextTv.text = skillsString
            } else {
                keySkillsTitleTv.visibility = View.GONE
                keySkillsTitleTv.visibility = View.GONE
            }
            val contacts = details.contacts
            Log.d("VacancyFragment", "contacts ${contacts.toString()}")
            if (contacts != null) {
                val name = contacts.name
                val email = contacts.email
                val phones = contacts.phones
                if (name != null) {
                    contactPersonTextTv.text = contacts.name
                } else {
                    contactPersonTitleTv.visibility = View.GONE
                    contactPersonTextTv.visibility = View.GONE
                }
                if (email != null) {
                    emailTextTv.text = contacts.email
                } else {
                    emailTextTv.visibility = View.GONE
                    emailTitleTv.visibility = View.GONE
                }
                if (!phones.isNullOrEmpty()) {
                    val phone = contacts.phones.first()
                    phoneTextTv.text = phone.number
                    val comment = phone.comment
                    if (!comment.isNullOrEmpty()) {
                        commentTextTv.text = comment
                    } else {
                        commentTextTv.visibility = View.GONE
                    }
                } else {
                    phoneTextTv.visibility = View.GONE
                    phoneTitleTv.visibility = View.GONE
                }
                if (name == null && email == null && phones.isNullOrEmpty()) {
                    contactsTitleTv.visibility = View.GONE
                    commentTitleTv.visibility = View.GONE
                }
            } else {
                contactsTitleTv.visibility = View.GONE
                contactPersonTitleTv.visibility = View.GONE
                contactPersonTextTv.visibility = View.GONE
                emailTextTv.visibility = View.GONE
                emailTitleTv.visibility = View.GONE
                phoneTextTv.visibility = View.GONE
                phoneTitleTv.visibility = View.GONE
                commentTitleTv.visibility = View.GONE
                commentTextTv.visibility = View.GONE
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

    private fun salaryFromAndToText(salary: Salary): String {
        val from = salary.from?.let { formatter(it) }
        val to = salary.to?.let { formatter(it) }
        return getString(
            R.string.vacancy_salary_text_full,
            from,
            to,
            currencyUTF(salary.currency)
        )
    }

    private fun salaryFromText(salary: Salary): String {
        val from = salary.from?.let { formatter(it) }
        return getString(
            R.string.vacancy_salary_text_from,
            from,
            currencyUTF(salary.currency)
        )
    }

    private fun salaryToText(salary: Salary): String {
        val to = salary.to?.let { formatter(it) }
        return getString(
            R.string.vacansy_salary_text_to,
            to,
            currencyUTF(salary.currency)
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
