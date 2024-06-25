package ru.practicum.android.diploma.filter.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterRegionBinding
import ru.practicum.android.diploma.filter.domain.models.Area
import ru.practicum.android.diploma.filter.presentation.FilterRegionViewModel
import ru.practicum.android.diploma.filter.ui.adapters.RegionAdapter
import ru.practicum.android.diploma.filter.ui.models.AreaUiState
import ru.practicum.android.diploma.filter.ui.models.RegionUiEvent
import ru.practicum.android.diploma.search.domain.models.Errors
import ru.practicum.android.diploma.util.BindingFragment

class FilterRegionFragment : BindingFragment<FragmentFilterRegionBinding>() {

    private val viewModel: FilterRegionViewModel by viewModel()
    private var dataToBeResumed: Boolean = false
    private val adapter: RegionAdapter by lazy {
        RegionAdapter { item ->
            backToPrevious(item)
        }
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFilterRegionBinding {
        return FragmentFilterRegionBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
        initializeList()
        setRequestInputBehaviour()
    }

    override fun onStart() {
        super.onStart()
        subscribeOnViewModel()
    }

    override fun onStop() {
        if (dataToBeResumed) {
            viewModel.onUiEvent(RegionUiEvent.ResumeData)
        }
        super.onStop()
    }

    private fun subscribeOnViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect {
                onUiState(it)
            }
        }
    }

    private fun onUiState(state: AreaUiState) {
        dataToBeResumed = state.dataToBeResumed
        when (state) {
            is AreaUiState.Default -> onDefaultState(state)
            is AreaUiState.EmptyResult -> onEmptyResult()
            is AreaUiState.Error -> onFirstRequestError(state.error)
            is AreaUiState.Loading -> onLoading()
            is AreaUiState.SearchResult -> showSearchResult(state)
            else -> {}
        }
        renderViews(state)
    }

    private fun renderViews(state: AreaUiState) {
        with(binding) {
            clearRegionIconIv.apply {
                isEnabled = state.clearEnabled
                setImageResource(state.clearIcon)
            }
            regionPictureIv.apply {
                isVisible = state.placeholderImageIsVisible
                state.placeholderImageIcon?.let { setImageResource(it) }
            }
            regionPictureTextTv.isVisible = state.placeholderMessageIsVisible
            regionListRv.isVisible = state.listRvIsVisible
            regionProgressPg.isVisible = state.progressBarIsVisible
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onDefaultState(result: AreaUiState.Default) {
        adapter.content = result.content.toMutableList()
        with(binding) {
            regionFieldEt.apply {
                text = null
                clearFocus()
            }
            regionListRv.apply {
                adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun onEmptyResult() {
        with(binding) {
            regionPictureTextTv.setText(R.string.no_region)
        }
    }

    private fun onErrorMessage(error: Errors): String {
        return when (error) {
            is Errors.ConnectionError -> getString(R.string.region_error_text)
            is Errors.ServerError -> getString(R.string.region_error_text)
            is Errors.IncorrectRequest -> getString(R.string.region_error_text)
            is Errors.Error404 -> getString(R.string.region_error_text)
        }
    }

    private fun onFirstRequestError(error: Errors) {
        binding.regionPictureTextTv.text = onErrorMessage(error)
    }

    private fun onLoading() {
        hideKeyboard()
    }

    private fun initializeList() {
        adapter.content = mutableListOf()
        binding.regionListRv.adapter = adapter
    }

    private fun setRequestInputBehaviour() {
        binding.regionFieldEt.doOnTextChanged { s, _, _, _ ->
            viewModel.onUiEvent(RegionUiEvent.QueryInput(s.toString()))
        }
    }

    private fun setOnClickListeners() {
        with(binding) {
            clearRegionIconIv.setOnClickListener {
                viewModel.onUiEvent(RegionUiEvent.ClearText)
            }

            regionBackArrowButton.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showSearchResult(result: AreaUiState.SearchResult) {
        adapter.content = result.content.toMutableList()
        with(binding) {
            regionListRv.apply {
                adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(
            binding.regionFieldEt.windowToken,
            0
        )
    }

    private fun backToPrevious(area: Area) {
        viewModel.saveSettings(area)
        findNavController().navigateUp()
    }
}
