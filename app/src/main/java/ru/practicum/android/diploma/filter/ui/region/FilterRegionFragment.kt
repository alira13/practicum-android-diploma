package ru.practicum.android.diploma.filter.ui.region

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterRegionBinding
import ru.practicum.android.diploma.filter.presentation.FilterRegionViewModel
import ru.practicum.android.diploma.filter.ui.region.adapter.RegionAdapter
import ru.practicum.android.diploma.filter.ui.region.models.RegionUiEvent
import ru.practicum.android.diploma.filter.ui.region.models.RegionUiState
import ru.practicum.android.diploma.search.domain.models.Errors
import ru.practicum.android.diploma.util.BindingFragment

class FilterRegionFragment : BindingFragment<FragmentFilterRegionBinding>() {

    private val viewModel: FilterRegionViewModel by viewModel()
    private var dataToBeResumed: Boolean = false
    private val adapter: RegionAdapter by lazy {
        RegionAdapter { item ->
            backToPrevious(item.id)
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

    private fun onUiState(state: RegionUiState) {
        dataToBeResumed = state.dataToBeResumed
        when (state) {
            is RegionUiState.Default -> onDefaultState()
            is RegionUiState.EmptyResult -> onEmptyResult()
            is RegionUiState.FirstRequestError -> onFirstRequestError(state.error)
            is RegionUiState.Loading -> onLoading()
            is RegionUiState.SearchResult -> showSearchResult(state)
            else -> {}
        }
        renderViews(state)
    }

    private fun renderViews(state: RegionUiState) {
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

    private fun onDefaultState() {
        with(binding) {
            regionFieldEt.apply {
                text = null
                clearFocus()
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
            is Errors.ConnectionError -> getString(R.string.no_internet)
            is Errors.ServerError -> getString(R.string.server_error_text)
            is Errors.IncorrectRequest -> getString(R.string.incorrect_request_text)
            is Errors.Error404 -> getString(R.string.server_error_text)
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
    private fun showSearchResult(result: RegionUiState.SearchResult) {
        adapter.content = result.content.toMutableList()
        with(binding) {
            regionListRv.apply {
                adapter?.notifyDataSetChanged()
                if (result.isItFirstPage) {
                    smoothScrollToPosition(0)
                }
                if (result.isFullLoaded) {
                    clearOnScrollListeners()
                } else {
                    setScrollListener(this)
                }
            }
        }
    }

    private fun setScrollListener(listRV: RecyclerView) {
        listRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    val pos = (
                        listRV.layoutManager as LinearLayoutManager
                        ).findLastVisibleItemPosition()
                    val itemsCount = adapter.itemCount
                    if (pos >= itemsCount - 1) {
                        viewModel.onUiEvent(RegionUiEvent.LastItemReached)
                    }
                }
            }
        })
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

    private fun backToPrevious(regionId: String) {
        viewModel.saveRegion(regionId)
        findNavController().navigateUp()
    }
}
