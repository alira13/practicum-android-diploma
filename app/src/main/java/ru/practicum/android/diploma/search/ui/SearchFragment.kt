package ru.practicum.android.diploma.search.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
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
import java.util.Locale

class SearchFragment : BindingFragment<FragmentSearchBinding>() {

    private var lastRequest: String? = null
        /*set(value) {
            Log.d("QQQ", "set() $value")
            field = value
        }
        get() = field.also{
            Log.d("QQQ", "get() $it")
        }*/
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
        initializeVacanciesList()
        setRequestInputBehaviour()

    }

    override fun onStart() {
        super.onStart()
        subscribeOnViewModel()
    }

    private fun subscribeOnViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
                viewModel.uiState.collect {
                    render(it)
                    if(it is SearchUiState.SearchResult) Log.d("QQQ", "${it.javaClass}")
                    else Log.d("QQQ","$it")
                /*}*/
            }
        }
    }

    private fun render(state: SearchUiState) {
        when (state) {
            SearchUiState.Default -> renderDefaultState()
            SearchUiState.EditingRequest -> onEditingRequest()
            SearchUiState.EmptyResult -> showEmptyResult()
            is SearchUiState.Error -> onError(state)
            is SearchUiState.Loading -> showLoading(state.isItFirstPage)
            SearchUiState.FullLoaded -> showFullLoaded()
            is SearchUiState.SearchResult -> showSearchResult(state)
        }
    }

    private fun renderDefaultState() {
        with(binding) {
            searchInputEt.apply {
                text = null
                clearFocus()
            }
            searchCountTv.isVisible = false
            searchProgressBar.isVisible = false
            searchListRv.isVisible = false
            searchPlaceholderMessageTv.isVisible = false
            searchClearIv.apply {
                isEnabled = false
                setImageResource(R.drawable.ic_search)
            }
            searchPlaceholderImageIv.apply {
                isVisible = true
                setImageResource(R.drawable.placeholder_main)
            }
        }
    }

    private fun onEditingRequest() {
        with(binding) {
            searchCountTv.isVisible = false
            searchProgressBar.isVisible = false
            searchListRv.isVisible = false
            searchPlaceholderMessageTv.isVisible = false
            searchClearIv.apply {
                isEnabled = true
                setImageResource(R.drawable.ic_close)
            }
            searchPlaceholderImageIv.isVisible = false
        }
    }

    private fun showEmptyResult() {
        with(binding) {
            searchProgressBar.isVisible = false
            searchListRv.isVisible = false
            searchCountTv.apply {
                setText(R.string.no_vacancies)
                isVisible = true
            }
            searchPlaceholderMessageTv.apply {
                isVisible = true
                setText(R.string.no_vacancies)
            }
            searchPlaceholderImageIv.apply {
                isVisible = true
                setImageResource(R.drawable.placeholder_error)
            }
        }
    }

    private fun onError(state: SearchUiState.Error) {
        val errorMessage = when (state.error) {
            is Errors.ConnectionError -> getString(R.string.no_internet)
            is Errors.ServerError -> getString(R.string.server_error_text)
            is Errors.IncorrectRequest -> getString(R.string.incorrect_request_text)
        }
        hideKeyboard()
        if (state.isItFirstPage) {
            with(binding) {
                searchCountTv.isVisible = false
                searchProgressBar.isVisible = false
                searchListRv.isVisible = false
                searchPlaceholderImageIv.apply {
                    isVisible = true
                    setImageResource(R.drawable.placeholder_internet_error)
                }
                searchPlaceholderMessageTv.apply {
                    isVisible = true
                    text = errorMessage
                }
            }
        } else {
            showToast(errorMessage)
        }
    }

    private fun showLoading(isItFirstPage: Boolean) {
        if (isItFirstPage) {
            hideKeyboard()
            with(binding) {
                searchCountTv.isVisible = false
                searchListRv.isVisible = false
                searchPlaceholderMessageTv.isVisible = false
                searchPlaceholderImageIv.isVisible = false
                searchProgressBar.isVisible = true
            }
        } else {
            binding.searchProgressBarPg.isVisible = true
        }
    }

    private fun initializeVacanciesList() {
        vacanciesAdapter.vacancies = emptyList()
        binding.searchListRv.adapter = vacanciesAdapter
    }

    private fun setRequestInputBehaviour() {
        binding.searchInputEt.doOnTextChanged { text, start, before, count ->
            if (
                !text.isNullOrEmpty()
                && text.toString() != lastRequest
            ) {
                lastRequest = text.toString()
                viewModel.onUiEvent(SearchUiEvent.QueryInput(text))
            }
        }
    }

    private fun showFullLoaded() {
        with(binding) {
            searchListRv.isVisible = true
            searchPlaceholderMessageTv.isVisible = false
            searchPlaceholderImageIv.isVisible = false
            searchProgressBar.isVisible = false
        }
    }

    private fun setOnClickListeners() {
        with(binding) {
            searchFilterBt.setOnClickListener {
                findNavController().navigate(R.id.action_searchFragment_to_filterSettingsFragment)
            }
            searchClearIv.setOnClickListener {
                viewModel.onUiEvent(SearchUiEvent.ClearText)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showSearchResult(result: SearchUiState.SearchResult) {
        with(binding) {
            vacanciesAdapter.vacancies = result.vacancies
            searchPlaceholderMessageTv.isVisible = false
            searchPlaceholderImageIv.isVisible = false
            binding.searchProgressBarPg.isVisible = false
            searchCountTv.apply {
                text = convertToPlurals(result.count.toInt())
                isVisible = true
            }
            searchListRv.apply {
                adapter?.notifyDataSetChanged()
                isVisible = true
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)

                        if (dy > 0) {
                            val pos = (searchListRv.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                            val itemsCount = vacanciesAdapter.itemCount
                            if (pos >= itemsCount - 1) {
                                viewModel.onUiEvent(SearchUiEvent.LastItemReached)
                            }
                        }
                    }
                })
            }
            searchProgressBar.isVisible = false
        }
    }

    private fun showToast(message: String) {
        val snackBar = Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT)
        snackBar.setTextColor(requireContext().getColor(R.color.white))
        snackBar.show()
        val viewSnackbar = snackBar.view.apply {
            setBackgroundResource(R.drawable.background_red_snackbar)
        }
        val textSnackbar: TextView =
            viewSnackbar.findViewById(com.google.android.material.R.id.snackbar_text)
        textSnackbar.textAlignment = View.TEXT_ALIGNMENT_CENTER
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(
            binding.searchInputEt.windowToken,
            0
        )
    }

    private fun toVacancyFullInfo(vacancyID: String) {
        findNavController().navigate(
            R.id.action_searchFragment_to_vacancyFragment,
            VacancyFragment.createArgs(vacancyID)
        )
    }

    private fun convertToPlurals(count: Int): String {
        val languageTag = "ru"
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.create(Locale.forLanguageTag(languageTag))
        )
        return resources.getQuantityString(R.plurals.vacancies_amount, count, count)
    }
}
