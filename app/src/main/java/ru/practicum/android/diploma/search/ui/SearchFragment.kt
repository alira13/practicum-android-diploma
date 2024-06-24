package ru.practicum.android.diploma.search.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.search.domain.models.Errors
import ru.practicum.android.diploma.search.domain.models.ProgressBarItem
import ru.practicum.android.diploma.search.presentation.SearchVacanciesViewModel
import ru.practicum.android.diploma.search.ui.models.SearchUiEvent
import ru.practicum.android.diploma.search.ui.models.SearchUiState
import ru.practicum.android.diploma.util.BindingFragment
import ru.practicum.android.diploma.vacancy.ui.VacancyFragment

class SearchFragment : BindingFragment<FragmentSearchBinding>() {

    private val viewModel: SearchVacanciesViewModel by viewModel()
    private var dataToBeResumed: Boolean = false
    private val vacanciesAdapter: VacanciesAdapter by lazy {
        VacanciesAdapter { vacancy ->
            toVacancyFullInfo(vacancy.id)
        }
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
        initializeVacanciesList()
        setRequestInputBehaviour()
    }

    override fun onStart() {
        super.onStart()
        subscribeOnViewModel()
        subscribeOnFilterState()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onUiEvent(SearchUiEvent.OnFragmentResume)
    }

    override fun onStop() {
        if (dataToBeResumed) {
            viewModel.onUiEvent(SearchUiEvent.ResumeData)
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

    private fun subscribeOnFilterState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.filterOnState.collect { filterState ->
                renderFilter(filterState)
            }
        }
    }

    private fun onUiState(state: SearchUiState) {
        dataToBeResumed = state.dataToBeResumed
        when (state) {
            is SearchUiState.Default -> onDefaultState()
            is SearchUiState.EmptyResult -> onEmptyResult()
            is SearchUiState.FirstRequestError -> onFirstRequestError(state.error)
            is SearchUiState.PagingError -> onPagingError(state.error)
            is SearchUiState.Loading -> onLoading()
            is SearchUiState.PagingLoading -> onPagingLoading()
            is SearchUiState.SearchResult -> showSearchResult(state)
            else -> {}
        }
        renderViews(state)
    }

    private fun renderViews(state: SearchUiState) {
        with(binding) {
            searchClearIv.apply {
                isEnabled = state.clearEnabled
                setImageResource(state.clearIcon)
            }
            searchCountTv.isVisible = state.countIsVisible
            searchPlaceholderImageIv.apply {
                isVisible = state.placeholderImageIsVisible
                state.placeholderImageIcon?.let { setImageResource(it) }
            }
            searchPlaceholderMessageTv.isVisible = state.placeholderMessageIsVisible
            searchListRv.isVisible = state.vacanciesListRvIsVisible
            searchProgressBar.isVisible = state.progressBarIsVisible
        }
    }

    private fun onDefaultState() {
        with(binding) {
            searchInputEt.apply {
                text = null
                clearFocus()
            }
        }
    }

    private fun onEmptyResult() {
        with(binding) {
            searchCountTv.setText(R.string.no_vacancies)
            searchPlaceholderMessageTv.setText(R.string.no_vacancies)
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

    private fun onPagingErrorMessage(error: Errors): String {
        return when (error) {
            is Errors.ConnectionError -> getString(R.string.check_internet)
            else -> getString(R.string.mistake_happened)
        }
    }

    private fun onPagingError(error: Errors) {
        vacanciesAdapter.vacancies.remove(ProgressBarItem)
        binding.searchListRv.adapter?.notifyItemRemoved(vacanciesAdapter.vacancies.size)
        showToast(onPagingErrorMessage(error))
    }

    private fun onFirstRequestError(error: Errors) {
        binding.searchPlaceholderMessageTv.text = onErrorMessage(error)
    }

    private fun onLoading() {
        hideKeyboard()
    }

    private fun onPagingLoading() {
        vacanciesAdapter.vacancies.add(ProgressBarItem)
        binding.searchListRv.adapter?.notifyItemInserted(vacanciesAdapter.vacancies.size)
    }

    private fun initializeVacanciesList() {
        vacanciesAdapter.vacancies = mutableListOf()
        binding.searchListRv.adapter = vacanciesAdapter
    }

    private fun setRequestInputBehaviour() {
        binding.searchInputEt.doOnTextChanged { s, _, _, _ ->
            viewModel.onUiEvent(SearchUiEvent.QueryInput(s.toString()))
        }
    }

    private fun setOnClickListeners() {
        with(binding) {
            searchFilterOffBt.setOnClickListener {
                toSettingsFilter()
            }
            searchFilterOnBt.setOnClickListener {
                toSettingsFilter()
            }
            searchClearIv.setOnClickListener {
                viewModel.onUiEvent(SearchUiEvent.ClearText)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showSearchResult(result: SearchUiState.SearchResult) {
        vacanciesAdapter.vacancies = result.vacancies.toMutableList()
        with(binding) {
            searchCountTv.text = result.count
            searchListRv.apply {
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
                    val pos = (listRV.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    val itemsCount = vacanciesAdapter.itemCount
                    if (pos >= itemsCount - 1) {
                        viewModel.onUiEvent(SearchUiEvent.LastItemReached)
                    }
                }
            }
        })
    }

    private fun renderFilter(isFilterOn: Boolean) {
        with(binding) {
            searchFilterOnBt.isVisible = isFilterOn
            searchFilterOffBt.isVisible = !isFilterOn
        }
    }

    private fun showToast(message: String) {
        val snackBar = Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT)
        snackBar.setTextColor(requireContext().getColor(R.color.white))
        snackBar.show()
        val viewSnackbar = snackBar.view.apply {
            setBackgroundResource(R.drawable.background_red_snackbar)
        }
        val textSnackbar: TextView = viewSnackbar.findViewById(com.google.android.material.R.id.snackbar_text)
        textSnackbar.textAlignment = View.TEXT_ALIGNMENT_CENTER
    }

    private fun hideKeyboard() {
        val inputMethodManager = requireContext().getSystemService(
            Context.INPUT_METHOD_SERVICE
        ) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(binding.searchInputEt.windowToken, 0)
    }

    private fun toSettingsFilter() {
        findNavController().navigate(R.id.action_searchFragment_to_filterSettingsFragment)
    }

    private fun toVacancyFullInfo(vacancyID: String) {
        findNavController().navigate(
            R.id.action_searchFragment_to_vacancyFragment,
            VacancyFragment.createArgs(vacancyID)
        )
    }
}
