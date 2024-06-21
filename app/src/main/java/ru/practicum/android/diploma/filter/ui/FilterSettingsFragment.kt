package ru.practicum.android.diploma.filter.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.google.android.material.color.MaterialColors
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterSettingsBinding
import ru.practicum.android.diploma.filter.domain.models.Settings
import ru.practicum.android.diploma.filter.presentation.FilterSettingsViewModel
import ru.practicum.android.diploma.util.BindingFragment

class FilterSettingsFragment : BindingFragment<FragmentFilterSettingsBinding>() {

    private var salary = 0
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
        setOnClickListener()
        inputSalary()
        salaryFocusChangeListener()
        subscribeOnSettingsState()
        readSettings()
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
                findNavController().popBackStack()
            }
            fsIvClearTextButton.setOnClickListener {
                fsEtSalary.text.clear()
                salary = 0
            }
            fsCbWithSalaryCheckbox.setOnClickListener {
                buttonGroup.isVisible = true
                onlyWithSalary = fsCbWithSalaryCheckbox.isChecked
            }
            fsIvClearPlaceWorkButton.setOnClickListener {
                viewModel.clearPlaceWork()
            }
            fsIvClearIndustryButton.setOnClickListener {
                viewModel.clearIndustry()
            }
            fsTvApplyButton.setOnClickListener {
                viewModel.saveSalarySettings(salary)
                viewModel.savaOnlyWithSalary(onlyWithSalary)
                findNavController().popBackStack()
            }
            fsTvResetButton.setOnClickListener {
                viewModel.resetSettings()
                findNavController().popBackStack()
            }
        }
    }

    private fun inputSalary() {
        binding.fsEtSalary.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    //
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    salary = if (s.toString().isEmpty()) {
                        0
                    } else {
                        s.toString().toInt()
                    }
                    salaryChangedListener(salary)
                }

                override fun afterTextChanged(s: Editable?) {
                    //
                }
            }
        )
    }

    private fun salaryChangedListener(salary: Int) {
        with(binding) {
            fsIvClearTextButton.isVisible = salary != 0
            buttonGroup.isVisible = true
        }
        renderEditTextFocusOffTextOff(editTextFocus, salary)
        renderEditTextFocusOffTextOn(editTextFocus, salary)
    }

    private fun salaryFocusChangeListener() {
        with(binding) {
            fsEtSalary.setOnFocusChangeListener { v, hasFocus ->
                editTextFocus = hasFocus
                renderEditTextFocusOffTextOff(editTextFocus, salary)
                renderEditTextFocusOnTextOff(editTextFocus, salary)
                renderEditTextFocusOnTextOn(editTextFocus, salary)
                renderEditTextFocusOffTextOn(editTextFocus, salary)
            }
        }
    }

    private fun readSettings() {
        viewModel.readSettings()
    }

    private fun subscribeOnSettingsState() {
        viewModel.getSettingsState().observe(viewLifecycleOwner) { settingsState ->
            renderSettingsState(settingsState)
        }
    }

    private fun renderEditTextFocusOffTextOff(
        focus: Boolean,
        salary: Int
    ) {
        with(binding) {
            if (!focus && salary == 0) {
                fsTvHintTitle.setTextColor(MaterialColors.getColor(fsTvHintTitle, R.attr.colorOnSecondary))
            }
        }
    }

    private fun renderEditTextFocusOnTextOff(
        focus: Boolean,
        salary: Int
    ) {
        with(binding) {
            if (focus && salary == 0) {
                fsTvHintTitle.setTextColor(resources.getColor(R.color.blue, null))
            }
        }
    }

    private fun renderEditTextFocusOnTextOn(
        focus: Boolean,
        salary: Int
    ) {
        with(binding) {
            if (focus && salary != 0) {
                fsTvHintTitle.setTextColor(resources.getColor(R.color.blue, null))
                fsIvClearTextButton.isVisible = true
            }
        }
    }

    private fun renderEditTextFocusOffTextOn(
        focus: Boolean,
        salary: Int
    ) {
        with(binding) {
            if (!focus && salary != 0) {
                fsTvHintTitle.setTextColor(resources.getColor(R.color.black, null))
                fsIvClearTextButton.isVisible = false
            }
        }
    }

    private fun renderSettingsState(settingsState: Settings) {
        renderPlaceWork(settingsState)
        renderIndustry(settingsState)
        renderSalary(settingsState)
        renderOnlyWithSalary(settingsState)
    }

    private fun renderSalary(settingsState: Settings) {
        with(binding) {
            if (settingsState.salary != 0) {
                fsEtSalary.setText(settingsState.salary.toString())
                editTextFocus = false
                salary = settingsState.salary
            }
        }
    }

    private fun renderOnlyWithSalary(settingsState: Settings) {
        with(binding) {
            fsCbWithSalaryCheckbox.isChecked = settingsState.onlyWithSalary
            onlyWithSalary = settingsState.onlyWithSalary
        }
    }

    private fun renderIndustry(settingsState: Settings) {
        with(binding) {
            if (settingsState.industry.id.isNotEmpty()) {
                industryValueGroup.isVisible = true
                industryTitleGroup.isInvisible = true
                fsTvIndustryValue.text = settingsState.industry.name
            } else {
                industryValueGroup.isInvisible = true
                industryTitleGroup.isVisible = true
            }
        }
    }

    private fun renderPlaceWork(settingsState: Settings) {
        with(binding) {
            if (settingsState.area.id.isNotEmpty()) {
                placeWorkValueGroup.isVisible = true
                placeWorkTitleGroup.isInvisible = true
                fsTvPlaceWorkValue.text = settingsState.area.name
            } else {
                placeWorkValueGroup.isInvisible = true
                placeWorkTitleGroup.isVisible = true
            }
        }
    }
}
