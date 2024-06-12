package ru.practicum.android.diploma.filter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterLocationBinding
import ru.practicum.android.diploma.util.BindingFragment

class FilterLocationFragment : BindingFragment<FragmentFilterLocationBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFilterLocationBinding {
        return FragmentFilterLocationBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        testNavigation()

    }

    private fun testNavigation() {
        binding.flTvTestButton1.setOnClickListener {
            findNavController().navigate(R.id.action_filterLocationFragment_to_filterCountryFragment)
        }
        binding.flTvTestButton2.setOnClickListener {
            findNavController().navigate(R.id.action_filterLocationFragment_to_filterRegionFragment)
        }
    }
}
