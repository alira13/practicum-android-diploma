package ru.practicum.android.diploma.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.search.presentation.SearchVacanciesViewModel
import ru.practicum.android.diploma.search.ui.models.SearchUiEvent
import ru.practicum.android.diploma.search.ui.models.SearchUiState
import ru.practicum.android.diploma.util.BindingFragment
import ru.practicum.android.diploma.vacancy.ui.VacancyFragment

class SearchFragment : BindingFragment<FragmentSearchBinding>() {

    private val viewModel: SearchVacanciesViewModel by viewModel()

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
    }

    private fun subscribeOnViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect {
                render(it)
            }
        }
    }

    private fun render(state: SearchUiState) {
        when (state) {
            SearchUiState.Default -> renderDefaultState()
            SearchUiState.EdittingRequest -> onEdittingRequest()
            SearchUiState.EmptyResult -> showEmptyResult()
            SearchUiState.Error -> onError()
            SearchUiState.Loading -> showLoading()
            is SearchUiState.SearchResult -> TODO()
        }
    }

    private fun renderDefaultState() {
        with(binding) {
            searchProgressPb.isVisible = false
            searchListRv.isVisible = false
            searchPictureTextTv.isVisible = false
            clearSearchIconIv.setImageResource(R.drawable.ic_search)
            searchPictureIv.apply {
                isVisible = true
                setImageResource(R.drawable.placeholder_main)
            }
        }
    }

    private fun onEdittingRequest() {
        with(binding) {
            searchProgressPb.isVisible = false
            searchListRv.isVisible = false
            searchPictureTextTv.isVisible = false
            clearSearchIconIv.setImageResource(R.drawable.ic_close)
            searchPictureIv.isVisible = false
        }
    }

    private fun showEmptyResult() {
        with(binding) {
            searchProgressPb.isVisible = false
            searchListRv.isVisible = false
            searchPictureTextTv.apply{
                isVisible = true
                setText(R.string.no_vacancies)
            }
            searchPictureIv.apply {
                isVisible = true
                setImageResource(R.drawable.placeholder_error)
            }
        }
    }

    private fun onError(){
        with(binding) {
            searchProgressPb.isVisible = false
            searchListRv.isVisible = false
            searchPictureTextTv.apply{
                isVisible = true
                setText(R.string.no_internet)
            }
            searchPictureIv.apply {
                isVisible = true
                setImageResource(R.drawable.placeholder_internet_error)
            }
        }
    }

    private fun showLoading(){
        with(binding) {
            searchListRv.isVisible = false
            searchPictureTextTv.isVisible = false
            searchPictureIv.isVisible = false
            searchProgressPb.isVisible = true
        }
    }

    private fun setOnClickListeners() {
        with(binding){
            searchFilterBt.setOnClickListener {
                findNavController().navigate(R.id.action_searchFragment_to_filterSettingsFragment)
            }
            clearSearchIconIv.setOnClickListener {
                viewModel.onUiEvent(SearchUiEvent.ClearText)
            }
        }

    }

    // клик на вакансию в списке
    private fun toVacancyFullInfo(vacancyID: String) {
        findNavController().navigate(
            R.id.action_searchFragment_to_vacancyFragment,
            VacancyFragment.createArgs(vacancyID)
        )
    }
}
