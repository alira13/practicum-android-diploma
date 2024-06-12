package ru.practicum.android.diploma.filter.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import ru.practicum.android.diploma.databinding.FragmentFilterRegionBinding
import ru.practicum.android.diploma.util.BindingFragment

class FilterRegionFragment : BindingFragment<FragmentFilterRegionBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFilterRegionBinding {
        return FragmentFilterRegionBinding.inflate(inflater, container, false)
    }
}
