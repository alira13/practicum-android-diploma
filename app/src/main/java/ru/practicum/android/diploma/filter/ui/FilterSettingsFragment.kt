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
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterSettingsBinding
import ru.practicum.android.diploma.util.BindingFragment

class FilterSettingsFragment : BindingFragment<FragmentFilterSettingsBinding>() {

    var salary = ""
    private var editTextFocus = false

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
            }
            fsCbWithSalaryCheckbox.setOnClickListener {
                buttonGroup.isVisible = fsCbWithSalaryCheckbox.isChecked || salary.isNotEmpty()
            }

        }
    }

    private fun inputSalary() {
        binding.fsEtSalary.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    salary = s.toString()
                    salaryChangedListener(salary)
                }

                override fun afterTextChanged(s: Editable?) {
                }
            }
        )
    }

    private fun salaryChangedListener(salary: String) {
        with(binding) {
            fsIvClearTextButton.isVisible = salary.isNotEmpty()
            if (!editTextFocus && salary.isEmpty()) {
                fsTvHintTitle.setTextColor(MaterialColors.getColor(fsTvHintTitle, R.attr.colorOnSecondary))
            }
            buttonGroup.isVisible = salary.isNotEmpty() || fsCbWithSalaryCheckbox.isChecked
        }
    }

    private fun salaryFocusChangeListener() {
        with(binding) {
            fsEtSalary.setOnFocusChangeListener { v, hasFocus ->
                editTextFocus = hasFocus

                if (!hasFocus && salary.isEmpty()) {
                    fsTvHintTitle.setTextColor(MaterialColors.getColor(fsTvHintTitle, R.attr.colorOnSecondary))
                }
                if (hasFocus && salary.isEmpty()) {
                    fsTvHintTitle.setTextColor(resources.getColor(R.color.blue, null))
                }
                if (hasFocus && salary.isNotEmpty()) {
                    fsTvHintTitle.setTextColor(resources.getColor(R.color.blue, null))
                    fsIvClearTextButton.isVisible = true
                }
                if (!hasFocus && salary.isNotEmpty()) {
                    fsTvHintTitle.setTextColor(resources.getColor(R.color.black, null))
                    fsIvClearTextButton.isVisible = false
                }
            }
        }
    }


}
