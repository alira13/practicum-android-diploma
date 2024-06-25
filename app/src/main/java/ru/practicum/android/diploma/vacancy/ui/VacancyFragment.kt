package ru.practicum.android.diploma.vacancy.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
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

open class VacancyFragment : BindingFragment<FragmentVacancyBinding>() {

    open val viewModel: VacancyDetailsViewModel by viewModel {
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
        viewModel.getFavoriteState().observe(viewLifecycleOwner) { stateFavorite ->
            renderFavoriteState(stateFavorite)
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
            favoriteOffIc.setOnClickListener {
                viewModel.addVacancyToFavorite(details)
            }
            favoriteOnIc.setOnClickListener {
                viewModel.deleteVacancyFromFavorite(details.id)
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
                DetailsShower(binding, requireContext()).showContent(state.details)
                setClickListeners(state.details)
                vacancyUrl = state.details.alternateUrl
            }
        }
    }

    private fun renderFavoriteState(stateFavorite: Boolean) {
        with(binding) {
            favoriteOnIc.isVisible = stateFavorite
            favoriteOffIc.isVisible = !stateFavorite
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

                is Errors.Error404 -> {
                    vacancyErrorPlaceHolderErrorText.text = getString(R.string.server_error_text)
                    showToast(getString(R.string.vacancy_was_deleted))
                    if (vacancyID != null) {
                        viewModel.deleteVacancyFromFavorite(vacancyID!!)
                    }
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

    private fun showToast(message: String) {
        val snackBar = Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT)
        snackBar.setTextColor(requireContext().getColor(R.color.white))
        snackBar.show()
        val viewSnackBar = snackBar.view.apply {
            setBackgroundResource(R.drawable.background_red_snackbar)
        }
        val textSnackBar: TextView =
            viewSnackBar.findViewById(com.google.android.material.R.id.snackbar_text)
        textSnackBar.textAlignment = View.TEXT_ALIGNMENT_CENTER
    }

    companion object {
        private const val VACANCY_ID = "vacancy_id"
        const val CORNER_RADIUS = 12

        fun createArgs(vacancyID: String): Bundle =
            bundleOf(VACANCY_ID to vacancyID)
    }
}
