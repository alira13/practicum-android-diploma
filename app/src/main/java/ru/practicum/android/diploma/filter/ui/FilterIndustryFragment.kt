package ru.practicum.android.diploma.filter.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.ext.android.inject
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterIndustryBinding
import ru.practicum.android.diploma.filter.domain.models.Industry
import ru.practicum.android.diploma.filter.presentation.FilterIndustryViewModel
import ru.practicum.android.diploma.filter.ui.adapters.IndustryAdapter
import ru.practicum.android.diploma.filter.ui.models.FilterUIState
import ru.practicum.android.diploma.search.domain.models.Errors
import ru.practicum.android.diploma.util.BindingFragment

class FilterIndustryFragment : BindingFragment<FragmentFilterIndustryBinding>() {

    private val viewModel by inject<FilterIndustryViewModel>()
    private val clickListener: (industry: Industry) -> Unit = { industry: Industry ->
        val checkedIndex = adapter.industries.indexOfFirst { it.isChecked }
        if (checkedIndex >= 0) {
            adapter.industries[checkedIndex].isChecked = false
        }
        if (adapter.industries.size != viewModel.industries.size) {
            viewModel.industries.forEach { it.isChecked = false }
        }
        industry.isChecked = !industry.isChecked
        adapter.update()
        viewModel.industry = industry
        binding.button.isVisible = true
    }

    private var adapter: IndustryAdapter = IndustryAdapter(clickListener)
    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            //
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s.isNullOrEmpty()) {
                showContent(viewModel.industries)
            } else {
                viewModel.filterIndustries(s.toString())
            }
        }

        override fun afterTextChanged(s: Editable?) {
            //
        }
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFilterIndustryBinding {
        return FragmentFilterIndustryBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUiState().observe(viewLifecycleOwner) { state ->
            render(state)
        }
        viewModel.getWriteComplete().observe(viewLifecycleOwner) { writeComplete ->
            if (writeComplete) {
                findNavController().popBackStack()
            }
        }
        binding.apply {
            fieldEt.addTextChangedListener(textWatcher)
            clearIconIv.setOnClickListener {
                fieldEt.text.clear()
            }
            button.setOnClickListener {
                viewModel.writeIndustry()
            }
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.apply {
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = adapter
        }
    }

    private fun render(state: FilterUIState) {
        when (state) {
            is FilterUIState.Content -> {
                showContent(state.industries)
            }

            is FilterUIState.Loading -> {
                showLoading()
            }

            is FilterUIState.Error -> {
                showError(state.errors)
            }

            is FilterUIState.Empty -> {
                showEmpty()
            }
        }
    }

    private fun showEmpty() {
        binding.apply {
            industryErrorPlaceHolderLayout.isVisible = true
            recyclerView.isVisible = false
            progressBar.isVisible = false
            industryErrorImage.setImageResource(R.drawable.placeholder_empty_location_list)
            industryErrorText.setText(R.string.empty_list)
        }
    }

    private fun showError(errors: Errors?) {
        binding.apply {
            industryErrorPlaceHolderLayout.isVisible = true
            recyclerView.isVisible = false
            progressBar.isVisible = false
            when (errors) {
                is Errors.ConnectionError -> {
                    industryErrorImage.setImageResource(R.drawable.placeholder_internet_error)
                    industryErrorText.setText(R.string.no_internet)
                }

                is Errors.Error404 -> {
                    industryErrorImage.setImageResource(R.drawable.placeholder_empty_location_list)
                    industryErrorText.setText(R.string.cant_get_list)
                }

                is Errors.ServerError -> {
                    industryErrorImage.setImageResource(R.drawable.placeholder_server_error)
                    industryErrorText.setText(R.string.server_error_text)
                }
                else -> {}
            }
        }
    }

    private fun showLoading() {
        binding.apply {
            recyclerView.isVisible = false
            progressBar.isVisible = true
            industryErrorPlaceHolderLayout.isVisible = false
        }
    }

    private fun showContent(industries: List<Industry>) {
        adapter.setItems(industries)
        binding.apply {
            recyclerView.isVisible = true
            progressBar.isVisible = false
            industryErrorPlaceHolderLayout.isVisible = false
        }
    }
}
