package ru.practicum.android.diploma.vacancy.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.search.domain.models.Errors
import ru.practicum.android.diploma.util.BindingFragment
import ru.practicum.android.diploma.util.currencyUTF
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
                salaryTv.isVisible = false
            }
            employerNameTv.text = details.employer?.name ?: ""
            val address = details.address
            if (address != null) {
                areaNameTv.text =
                    getString(R.string.vacancy_address_text, address.city, address.street, address.building)
            } else {
                areaNameTv.text = details.area.name
            }
            val logoUrls = details.employer?.logoUrls
            if (logoUrls != null) {
                provideLogo(employerLogoIv, logoUrls.px90)
            } else {
                employerLogoIv.setImageDrawable(
                    AppCompatResources.getDrawable(requireContext(), R.drawable.ic_placeholder)
                )
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
        return getString(
            R.string.vacancy_salary_text_full,
            salary.from.toString(),
            salary.to.toString(),
            currencyUTF(salary.currency)
        )
    }

    private fun salaryFromText(salary: Salary): String {
        return getString(
            R.string.vacancy_salary_text_from,
            salary.from.toString(),
            currencyUTF(salary.currency)
        )
    }

    private fun salaryToText(salary: Salary): String {
        return getString(
            R.string.vacansy_salary_text_to,
            salary.to.toString(),
            currencyUTF(salary.currency)
        )
    }

    private fun provideLogo(imageView: ImageView, url: String) {
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
