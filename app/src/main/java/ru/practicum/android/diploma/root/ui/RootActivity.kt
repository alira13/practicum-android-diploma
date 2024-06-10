package ru.practicum.android.diploma.root.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ActivityRootBinding
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.models.VacanciesSearchRequest
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailsInteractor
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetailsRequest

class RootActivity : AppCompatActivity() {

    private val vacancyDetailsInteractor by inject<VacancyDetailsInteractor>()
    private val searchInteractor by inject<SearchInteractor>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val isVisible = when (destination.id) {
                R.id.searchFragment,
                R.id.favoritesFragment,
                R.id.teamFragment -> true

                else -> false
            }

            binding.bottomNavigationView.isVisible = isVisible
            binding.line.isVisible = isVisible
        }
        lifecycleScope.launch {
            val details = vacancyDetailsInteractor.getVacancyDetails(
                VacancyDetailsRequest(id = "81430574")
            )
            Log.d("RootActivity", "details response $details")
            val search = searchInteractor.searchVacancies(
                VacanciesSearchRequest(page = 0, searchString = "разработчик")
            )
            Log.d("RootActivity", "search response $search")
        }
    }
}

/*
* Пример использования access token для HeadHunter API
* networkRequestExample(accessToken = BuildConfig.HH_ACCESS_TOKEN)
* private fun networkRequestExample(accessToken: String) {
        // ...
    }
* */
