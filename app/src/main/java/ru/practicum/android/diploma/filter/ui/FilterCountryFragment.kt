package ru.practicum.android.diploma.filter.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import ru.practicum.android.diploma.databinding.FragmentFilterCountryBinding
import ru.practicum.android.diploma.util.BindingFragment

class FilterCountryFragment : BindingFragment<FragmentFilterCountryBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFilterCountryBinding {
        return FragmentFilterCountryBinding.inflate(inflater, container, false)
    }
}
