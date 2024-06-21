package ru.practicum.android.diploma.filter.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentFilterCountryBinding
import ru.practicum.android.diploma.filter.domain.models.FilterCountryState
import ru.practicum.android.diploma.filter.domain.models.Region
import ru.practicum.android.diploma.filter.presentation.FilterCountryViewModel
import ru.practicum.android.diploma.util.BindingFragment

class FilterCountryFragment : BindingFragment<FragmentFilterCountryBinding>() {

    private val viewModel: FilterCountryViewModel by viewModel()

    private val countryAdapter: CountryAdapter by lazy {
        CountryAdapter()
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFilterCountryBinding {
        return FragmentFilterCountryBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        subscribeOnViewModel()
        setupSetOnClickListeners()
    }

    private fun setupSetOnClickListeners() {
        countryAdapter.onCountryClickListener = { country -> onCountryClick(country) }
        countryAdapter.onOtherRegionClickListener = { region -> Log.e("FilterCountryFragment999", region.name) }
    }

    private fun onCountryClick(region: Region) {
        viewModel.chooseCountry(region)
        findNavController().navigateUp()
    }

    private fun setupRecyclerView() {
        binding.rvCountry.adapter = countryAdapter
    }

    private fun subscribeOnViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                renderState(state)
            }
        }
    }

    private fun renderState(state: FilterCountryState) {
        when (state) {
            is FilterCountryState.Default -> showLoading()
            is FilterCountryState.Content -> showContent(state.countries)
            is FilterCountryState.Error -> showError()
        }
    }

    private fun showError() {
        binding.llError.visibility = View.VISIBLE
        binding.rvCountry.visibility = View.GONE
        binding.pbCountry.visibility = View.GONE
    }

    private fun showLoading() {
        binding.pbCountry.visibility = View.VISIBLE
        binding.rvCountry.visibility = View.GONE
        binding.llError.visibility = View.GONE
    }

    private fun showContent(regions: List<Region>) {
        binding.rvCountry.visibility = View.VISIBLE
        binding.pbCountry.visibility = View.GONE
        binding.llError.visibility = View.GONE

        countryAdapter.countries = regions.toMutableList()
    }
}
