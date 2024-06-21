package ru.practicum.android.diploma.filter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterLocationBinding
import ru.practicum.android.diploma.filter.presentation.FilterLocationViewModel
import ru.practicum.android.diploma.filter.ui.models.FilterLocationUiState
import ru.practicum.android.diploma.util.BindingFragment

class FilterLocationFragment : BindingFragment<FragmentFilterLocationBinding>() {

    private val viewModel: FilterLocationViewModel by viewModel()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFilterLocationBinding {
        return FragmentFilterLocationBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeOnViewModel()
        setNavigation()
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateFilters()
    }

    private fun subscribeOnViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect {
                onUiState(it)
            }
        }
    }

    private fun onUiState(state: FilterLocationUiState) {
        with(binding) {
            flApproveButton.isVisible = state.approveButtonVisibility
            if (state.item1.itemName == null) {
                flEtCountryName.text = null
            } else {
                flEtCountryName.setText(state.item1.itemName)
            }
            if (state.item2.itemName == null) {
                flEtRegionName.text = null
            } else {
                flEtRegionName.setText(state.item2.itemName)
            }
            flCountryEndIcon.setImageResource(state.item1.endIconRes)
            flTilCountryElement.setHintTextAppearance(state.item1.hintTextAppearance)
            flRegionEndIcon.setImageResource(state.item2.endIconRes)
            flTilRegionElement.setHintTextAppearance(state.item2.hintTextAppearance)
        }
    }

    private fun setNavigation() {
        binding.flCountryEndIcon.setOnClickListener {
            findNavController().navigate(R.id.action_filterLocationFragment_to_filterCountryFragment)
        }
        binding.flRegionEndIcon.setOnClickListener {
            findNavController().navigate(R.id.action_filterLocationFragment_to_filterRegionFragment)
        }
        binding.flIvBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.flApproveButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
