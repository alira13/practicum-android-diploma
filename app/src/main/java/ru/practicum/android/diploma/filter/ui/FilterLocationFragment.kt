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
import ru.practicum.android.diploma.filter.ui.models.FilterItem
import ru.practicum.android.diploma.filter.ui.models.FilterLocationUiEvent
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
        viewModel.onUiEvent(FilterLocationUiEvent.UpdateData)
    }

    private fun subscribeOnViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect {
                onUiState(it)
            }
        }
    }

    private fun onUiState(state: FilterLocationUiState) {
        val condition1 = state.item1 is FilterItem.Absent
        val condition2 = state.item2 is FilterItem.Absent
        with(binding) {
            flCountryEndIcon.apply{
                setOnClickListener {
                    setCountryEndIconClickListener(condition1)
                }
                setImageResource(state.item1.endIconRes)
            }
            flRegionEndIcon.apply{
                setOnClickListener {
                    setRegionEndIconClickListener(condition2)
                }
                setImageResource(state.item2.endIconRes)
            }
            flApproveButton.isVisible = state.approveButtonVisibility
            if (condition1) {
                flEtCountryName.text = null
            } else {
                flEtCountryName.setText(state.item1.itemName)
            }
            if (condition2) {
                flEtRegionName.text = null
            } else {
                flEtRegionName.setText(state.item2.itemName)
            }
            flTilCountryElement.setHintTextAppearance(state.item1.hintTextAppearance)
            flTilRegionElement.setHintTextAppearance(state.item2.hintTextAppearance)
        }
    }

    private fun setCountryEndIconClickListener(condition: Boolean) {
        if (condition) {
            findNavController().navigate(R.id.action_filterLocationFragment_to_filterCountryFragment)
        } else {
            viewModel.onUiEvent(FilterLocationUiEvent.ClearCountry)
        }
    }

    private fun setRegionEndIconClickListener(condition: Boolean) {
        if (condition) {
            findNavController().navigate(R.id.action_filterLocationFragment_to_filterRegionFragment)
        } else {
            viewModel.onUiEvent(FilterLocationUiEvent.ClearRegion)
        }
    }

    private fun setNavigation() {
        binding.flIvBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.flApproveButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
