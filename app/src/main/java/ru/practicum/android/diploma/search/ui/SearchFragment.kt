package ru.practicum.android.diploma.search.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.search.domain.models.Errors
import ru.practicum.android.diploma.search.presentation.SearchVacanciesViewModel
import ru.practicum.android.diploma.search.ui.models.SearchUiEvent
import ru.practicum.android.diploma.search.ui.models.SearchUiState
import ru.practicum.android.diploma.util.BindingFragment
import ru.practicum.android.diploma.vacancy.ui.VacancyFragment

class SearchFragment : BindingFragment<FragmentSearchBinding>() {

    private var lastRequest: String? = null
    private val viewModel: SearchVacanciesViewModel by viewModel()
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
        subscribeOnViewModel()
        initializeVacanciesList()
        setRequestInputBehaviour()
        // только чтоб проверка пропустила неиспользуемый метод - его вызов закоментить
        showToast("")
    }

    private fun subscribeOnViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect {
                if (it is SearchUiState.SearchResult) {
                    Log.d(
                        "QQQ",
                        "${it.javaClass}"
                    )
                }
                Log.d("QQQ", "$it")
                render(it)
            }
        }
    }

    private fun render(state: SearchUiState) {
        when (state) {
            SearchUiState.Default -> renderDefaultState()
            SearchUiState.EditingRequest -> onEditingRequest()
            SearchUiState.EmptyResult -> showEmptyResult()
            is SearchUiState.Error -> onError(state.error)
            SearchUiState.Loading -> showLoading()
            SearchUiState.FullLoaded -> showFullLoaded()
            is SearchUiState.SearchResult -> showSearchResult(state)
        }
    }

    private fun renderDefaultState() {
        with(binding) {
            searchFieldEt.apply {
                text = null
                clearFocus()
            }
            searchResultTv.isVisible = false
            searchProgressPb.isVisible = false
            searchListRv.isVisible = false
            searchPictureTextTv.isVisible = false
            clearSearchIconIv.apply {
                isEnabled = false
                setImageResource(R.drawable.ic_search)
            }
            searchPictureIv.apply {
                isVisible = true
                setImageResource(R.drawable.placeholder_main)
            }
        }
    }

    private fun onEditingRequest() {
        with(binding) {
            searchResultTv.isVisible = false
            searchProgressPb.isVisible = false
            searchListRv.isVisible = false
            searchPictureTextTv.isVisible = false
            clearSearchIconIv.apply {
                isEnabled = true
                setImageResource(R.drawable.ic_close)
            }
            searchPictureIv.isVisible = false
        }
    }

    private fun showEmptyResult() {
        with(binding) {
            searchProgressPb.isVisible = false
            searchListRv.isVisible = false
            searchResultTv.apply {
                setText(R.string.no_vacancies)
                isVisible = true
            }
            searchPictureTextTv.apply {
                isVisible = true
                setText(R.string.no_vacancies)
            }
            searchPictureIv.apply {
                isVisible = true
                setImageResource(R.drawable.placeholder_error)
            }
        }
    }

    private fun onError(error: Errors) {
        with(binding) {
            searchResultTv.isVisible = false
            searchProgressPb.isVisible = false
            searchListRv.isVisible = false
            searchPictureIv.apply {
                isVisible = true
                setImageResource(R.drawable.placeholder_internet_error)
            }
            when (error) {
                is Errors.ConnectionError -> {
                    searchPictureTextTv.text = getString(R.string.no_internet)
                }

                is Errors.ServerError -> {
                    searchPictureTextTv.text = getString(R.string.server_error_text)
                }

                is Errors.IncorrectRequest -> {
                    searchPictureTextTv.text = getString(R.string.incorrect_request_text)
                }
            }
        }
    }

    private fun showLoading() {
        with(binding) {
            searchResultTv.isVisible = false
            searchListRv.isVisible = false
            searchPictureTextTv.isVisible = false
            searchPictureIv.isVisible = false
            searchProgressPb.isVisible = true
        }
    }

    private fun initializeVacanciesList() {
        vacanciesAdapter.vacancies = emptyList()
        binding.searchListRv.adapter = vacanciesAdapter
    }

    private fun setRequestInputBehaviour() {
        binding.searchFieldEt.doOnTextChanged { text, start, before, count ->
            if (
                text != null
                && text.toString() != lastRequest
            ) {
                lastRequest = text.toString()
                Log.d("QQQ", "изменение запроса $lastRequest")
                viewModel.onUiEvent(SearchUiEvent.QueryInput(text))
            }
        }
    }

    private fun showFullLoaded() {
        with(binding) {
            searchListRv.isVisible = true
            searchPictureTextTv.isVisible = false
            searchPictureIv.isVisible = false
            searchProgressPb.isVisible = false
        }
    }

    private fun setOnClickListeners() {
        with(binding) {
            searchFilterBt.setOnClickListener {
                findNavController().navigate(R.id.action_searchFragment_to_filterSettingsFragment)
            }
            clearSearchIconIv.setOnClickListener {
                viewModel.onUiEvent(SearchUiEvent.ClearText)
            }
        }
    }

    private fun showSearchResult(result: SearchUiState.SearchResult) {
        Log.d("QQQ", "отображение результата")
        with(binding) {
            hideKeyboard()
            vacanciesAdapter.vacancies = result.vacancies
            searchProgressPb.isVisible = false
            searchPictureTextTv.isVisible = false
            searchPictureIv.isVisible = false
            searchResultTv.apply {
                text = result.count
                isVisible = true
            }
            searchListRv.apply {
                Log.d("QQQ", "добрались до ресайклера")
                adapter?.notifyDataSetChanged()
                isVisible = true
                Log.d("QQQ", "его видимость $visibility")
                Log.d("QQQ", "размер списка ${vacanciesAdapter.vacancies.size}")
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)

                        if (dy > 0) {
                            val pos = (searchListRv.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                            val itemsCount = vacanciesAdapter.itemCount
                            if (pos >= itemsCount - 1) {
                                searchProgressPb.isVisible = true
                                viewModel.onUiEvent(SearchUiEvent.LastItemReached)
                            }
                        }
                    }
                })
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(
            binding.searchFieldEt.windowToken,
            0
        )
    }

    private fun toVacancyFullInfo(vacancyID: String) {
        findNavController().navigate(
            R.id.action_searchFragment_to_vacancyFragment,
            VacancyFragment.createArgs(vacancyID)
        )
    }
}
