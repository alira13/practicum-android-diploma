package ru.practicum.android.diploma.vacancy.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.util.BindingFragment

class VacancyFragment : BindingFragment<FragmentVacancyBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentVacancyBinding {
        return FragmentVacancyBinding.inflate(inflater, container, false)
    }

    companion object {
        private const val VACANCY_ID = "vacancy_id"

        fun createArgs(vacancyID: String): Bundle =
            bundleOf(VACANCY_ID to vacancyID)
    }
}
