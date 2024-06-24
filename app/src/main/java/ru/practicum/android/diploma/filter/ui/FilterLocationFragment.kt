package ru.practicum.android.diploma.filter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
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
        binding.flApproveButton.isVisible = state.approveButtonVisibility
        customizeElement(
            item = state.item1,
            endIcon = binding.flCountryEndIcon,
            itemName = binding.flEtCountryName,
            element = binding.flTilCountryElement,
            elementType = ELEMENT_TYPE_COUNTRY
        )
        customizeElement(
            item = state.item2,
            endIcon = binding.flRegionEndIcon,
            itemName = binding.flEtRegionName,
            element = binding.flTilRegionElement,
            elementType = ELEMENT_TYPE_REGION
        )
    }

    private fun customizeElement(
        item: FilterItem,
        endIcon: ImageView,
        itemName: TextInputEditText,
        element: TextInputLayout,
        elementType: Int
    ) {
        val condition = item is FilterItem.Absent
        endIcon.apply {
            setOnClickListener {
                chooseOnClickListeners(condition, elementType)
            }
            setImageResource(item.endIconRes)
        }
        if (condition) {
            itemName.text = null
        } else {
            itemName.setText(item.itemName)
        }
        element.setHintTextAppearance(item.hintTextAppearance)
    }

    private fun chooseOnClickListeners(
        condition: Boolean,
        elementType: Int
    ) {
        when (elementType) {
            ELEMENT_TYPE_COUNTRY -> setCountryEndIconClickListener(condition)
            ELEMENT_TYPE_REGION -> setRegionEndIconClickListener(condition)
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
            viewModel.onUiEvent(FilterLocationUiEvent.ExitWithoutSavingChanges)
            findNavController().popBackStack()
        }
        binding.flApproveButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    companion object {
        private const val ELEMENT_TYPE_COUNTRY = 1
        private const val ELEMENT_TYPE_REGION = 2
    }
}
