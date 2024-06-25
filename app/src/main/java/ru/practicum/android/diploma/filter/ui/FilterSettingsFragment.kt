package ru.practicum.android.diploma.filter.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.google.android.material.color.MaterialColors
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterSettingsBinding
import ru.practicum.android.diploma.filter.domain.models.Industry
import ru.practicum.android.diploma.filter.domain.models.Settings
import ru.practicum.android.diploma.filter.presentation.FilterSettingsViewModel
import ru.practicum.android.diploma.util.BindingFragment

class FilterSettingsFragment : BindingFragment<FragmentFilterSettingsBinding>() {

    private var oldEditData = 0L
    private var newEditData = 0L
    private var editTextFocus = false
    private var onlyWithSalary = false

    private val viewModel by viewModel<FilterSettingsViewModel>()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFilterSettingsBinding {
        return FragmentFilterSettingsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        readNewSettings()
        subscribeOnSettingsState()
        inputSalary()
        setOnClickListener()
        salaryFocusChangeListener()
    }

    private fun setOnClickListener() {
        with(binding) {
            fsIvToPlaceWorkButton.setOnClickListener {
                findNavController().navigate(R.id.action_filterSettingsFragment_to_filterLocationFragment)
            }
            fsIvToIndustryButton.setOnClickListener {
                findNavController().navigate(R.id.action_filterSettingsFragment_to_filterIndustryFragment)
            }
            backArrowButton.setOnClickListener {
                viewModel.returnSavedSettings()
                findNavController().popBackStack()
            }
            fsIvClearTextButton.setOnClickListener {
                fsEtSalary.text.clear()
                newEditData = 0
            }
            fsCbWithSalaryCheckbox.setOnClickListener {
                buttonGroup.isVisible = true
                onlyWithSalary = fsCbWithSalaryCheckbox.isChecked
                viewModel.saveOnlyWithSalary(onlyWithSalary)
            }
            fsIvClearPlaceWorkButton.setOnClickListener {
                viewModel.clearPlaceWork()
                buttonGroup.isVisible = true
            }
            fsIvClearIndustryButton.setOnClickListener {
                viewModel.clearIndustry()
                buttonGroup.isVisible = true
            }
            fsTvApplyButton.setOnClickListener {
                viewModel.saveFilterSettings()
                findNavController().popBackStack()
            }
            fsTvResetButton.setOnClickListener {
                viewModel.resetSettings()
                viewModel.readSavedSettings()
            }
            requireActivity().onBackPressedDispatcher.addCallback(
                viewLifecycleOwner,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        viewModel.returnSavedSettings()
                        findNavController().popBackStack()
                    }
                }
            )
        }
    }

    private fun inputSalary() {
        binding.fsEtSalary.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    //
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    newEditData = if (s.toString().isEmpty()) {
                        0L
                    } else {
                        s.toString().toLong()
                    }
                    salaryChangedListener(newEditData)
                }

                override fun afterTextChanged(s: Editable?) {
                    //
                }
            }
        )
    }

    private fun salaryChangedListener(newEditData: Long) {
        viewModel.saveSalary(newEditData)
        with(binding) {
            fsIvClearTextButton.isVisible = newEditData != 0L
            buttonGroup.isVisible = newEditData != oldEditData
        }
        renderEditTextFocusOffTextOff(editTextFocus, newEditData)
        renderEditTextFocusOffTextOn(editTextFocus, newEditData)
    }

    private fun salaryFocusChangeListener() {
        with(binding) {
            fsEtSalary.setOnFocusChangeListener { v, hasFocus ->
                editTextFocus = hasFocus
                renderEditTextFocusOffTextOff(editTextFocus, newEditData)
                renderEditTextFocusOnTextOff(editTextFocus, newEditData)
                renderEditTextFocusOnTextOn(editTextFocus, newEditData)
                renderEditTextFocusOffTextOn(editTextFocus, newEditData)
            }
        }
    }

    private fun readNewSettings() {
        viewModel.readNewSettings()
    }

    private fun subscribeOnSettingsState() {
        viewModel.getSalaryState().observe(viewLifecycleOwner) { settingsState ->
            renderSalary(settingsState)
            if (settingsState.filterOn) {
                binding.fsTvResetButton.isVisible = true
            } else {
                binding.buttonGroup.isVisible = false
            }
        }
        viewModel.getOnlyWithSalaryState().observe(viewLifecycleOwner) { onlyWithSalary ->
            renderOnlyWithSalary(onlyWithSalary)
        }
        viewModel.getPlaceWorkState().observe(viewLifecycleOwner) { placeWork ->
            renderPlaceWork(placeWork)
        }
        viewModel.getIndustryState().observe(viewLifecycleOwner) { industry ->
            renderIndustry(industry)
        }
    }

    private fun renderEditTextFocusOffTextOff(
        focus: Boolean,
        salary: Long
    ) {
        if (!focus && salary == 0L) {
            binding.fsTvHintTitle.setTextColor(
                MaterialColors.getColor(
                    binding.fsTvHintTitle,
                    R.attr.colorOnSecondary
                )
            )
        }
    }

    private fun renderEditTextFocusOnTextOff(
        focus: Boolean,
        salary: Long
    ) {
        if (focus && salary == 0L) {
            binding.fsTvHintTitle.setTextColor(resources.getColor(R.color.blue, null))
        }
    }

    private fun renderEditTextFocusOnTextOn(
        focus: Boolean,
        salary: Long
    ) {
        if (focus && salary != 0L) {
            with(binding) {
                fsTvHintTitle.setTextColor(resources.getColor(R.color.blue, null))
                fsIvClearTextButton.isVisible = true
            }
        }
    }

    private fun renderEditTextFocusOffTextOn(
        focus: Boolean,
        salary: Long
    ) {
        if (!focus && salary != 0L) {
            with(binding) {
                fsTvHintTitle.setTextColor(resources.getColor(R.color.black, null))
                fsIvClearTextButton.isVisible = false
            }
        }
    }

    private fun renderSalary(settingsState: Settings) {
        with(binding) {
            if (settingsState.salary != 0L) {
                oldEditData = settingsState.salary
                fsEtSalary.setText(settingsState.salary.toString())
                editTextFocus = false

            } else {
                fsEtSalary.text = null
            }
        }
    }

    private fun renderOnlyWithSalary(onlyWithSalary: Boolean) {
        this.onlyWithSalary = onlyWithSalary
        binding.fsCbWithSalaryCheckbox.isChecked = onlyWithSalary
    }

    private fun renderIndustry(industryState: Industry) {
        with(binding) {
            if (industryState.id.isNotEmpty()) {
                industryValueGroup.isVisible = true
                industryTitleGroup.isInvisible = true
                fsTvIndustryValue.text = industryState.name
            } else {
                industryValueGroup.isInvisible = true
                industryTitleGroup.isVisible = true
            }
            fsTvIndustryValue.addTextChangedListener { buttonGroup.isVisible = true }
        }
    }

    private fun renderPlaceWork(placeWork: String) {
        with(binding) {
            if (placeWork.isNotEmpty()) {
                placeWorkValueGroup.isVisible = true
                placeWorkTitleGroup.isInvisible = true
                fsTvPlaceWorkValue.text = placeWork
            } else {
                placeWorkValueGroup.isInvisible = true
                placeWorkTitleGroup.isVisible = true
            }
            fsTvPlaceWorkValue.addTextChangedListener { buttonGroup.isVisible = true }
        }
    }
}
