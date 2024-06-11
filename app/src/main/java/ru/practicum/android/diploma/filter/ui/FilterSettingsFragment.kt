package ru.practicum.android.diploma.filter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterSettingsBinding
import ru.practicum.android.diploma.util.BindingFragment

class FilterSettingsFragment : BindingFragment<FragmentFilterSettingsBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFilterSettingsBinding {
        return FragmentFilterSettingsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        testNavigation()

    }

    private fun testNavigation() {
        binding.fsTvTestButton1.setOnClickListener {
            findNavController().navigate(R.id.action_filterSettingsFragment_to_filterLocationFragment)
        }
        binding.fsTvTestButton2.setOnClickListener {
            findNavController().navigate(R.id.action_filterSettingsFragment_to_filterIndustryFragment)
        }
    }
}
