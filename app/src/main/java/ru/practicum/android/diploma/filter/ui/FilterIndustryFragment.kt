package ru.practicum.android.diploma.filter.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import ru.practicum.android.diploma.databinding.FragmentFilterIndustryBinding
import ru.practicum.android.diploma.util.BindingFragment

class FilterIndustryFragment : BindingFragment<FragmentFilterIndustryBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFilterIndustryBinding {
        return FragmentFilterIndustryBinding.inflate(inflater, container, false)
    }
}
