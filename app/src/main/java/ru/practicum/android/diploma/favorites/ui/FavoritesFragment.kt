package ru.practicum.android.diploma.favorites.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFavoritesBinding
import ru.practicum.android.diploma.favoritedetails.ui.FavoriteDetailsFragment
import ru.practicum.android.diploma.favorites.presentation.FavoritesUiState
import ru.practicum.android.diploma.favorites.presentation.FavoritesViewModel
import ru.practicum.android.diploma.search.domain.models.VacancyPreview
import ru.practicum.android.diploma.search.ui.VacanciesAdapter
import ru.practicum.android.diploma.util.BindingFragment

class FavoritesFragment : BindingFragment<FragmentFavoritesBinding>() {

    private val viewModel: FavoritesViewModel by viewModel()

    private val vacanciesAdapter: VacanciesAdapter by lazy {
        VacanciesAdapter { vacancy -> toFavoriteVacancyDetails(vacancy.id) }
            .apply {
                vacancies = emptyList()
            }
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoritesBinding {
        return FragmentFavoritesBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeFavoritesList()

        viewModel.uiStateFlow.observe(viewLifecycleOwner) { state ->
            render(state)
        }
    }

    private fun render(state: FavoritesUiState) {
        when (state) {
            is FavoritesUiState.Content -> showContent(state.list)

            FavoritesUiState.Empty -> showError(
                R.drawable.placeholder_favorites_empty,
                R.string.empty_list
            )

            FavoritesUiState.Failure -> showError(
                R.drawable.placeholder_error,
                R.string.no_vacancies
            )
        }
    }

    private fun showContent(list: List<VacancyPreview>) {
        vacanciesAdapter.vacancies = list
        with(binding) {
            favRvVacancies.apply {
                adapter?.notifyDataSetChanged()
                isVisible = true
            }
            favPlaceholderIcon.isVisible = false
            favPlaceholderMessage.isVisible = false
        }
    }

    private fun showError(
        imageRes: Int,
        textRes: Int
    ) {
        with(binding) {
            favRvVacancies.isVisible = false
            favPlaceholderIcon.apply {
                isVisible = true
                setImageResource(imageRes)
            }
            favPlaceholderMessage.apply {
                isVisible = true
                setText(textRes)
            }
        }
    }

    private fun initializeFavoritesList() {
        vacanciesAdapter.vacancies = emptyList()
        binding.favRvVacancies.adapter = vacanciesAdapter
    }

    private fun toFavoriteVacancyDetails(vacancyID: String) {
        findNavController().navigate(
            R.id.action_favoritesFragment_to_favoriteDetailFragment,
            FavoriteDetailsFragment.createArgs(vacancyID)
        )
    }
}
