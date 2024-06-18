package ru.practicum.android.diploma.favoritedetails.ui

import android.os.Bundle
import androidx.core.os.bundleOf
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.practicum.android.diploma.favoritedetails.presentation.FavoriteDetailsViewModel
import ru.practicum.android.diploma.vacancy.ui.VacancyFragment

class FavoriteDetailsFragment : VacancyFragment() {

    override val viewModel: FavoriteDetailsViewModel by viewModel {
        parametersOf(favoriteId)
    }

    private val favoriteId: String? by lazy { requireArguments().getString(ARG_FAVORITE_ID) }

    companion object {
        const val ARG_FAVORITE_ID = "favorite"
        fun createArgs(favoriteId: String): Bundle = bundleOf(ARG_FAVORITE_ID to favoriteId)
    }
}
