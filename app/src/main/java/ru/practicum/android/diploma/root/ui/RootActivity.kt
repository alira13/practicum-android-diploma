package ru.practicum.android.diploma.root.ui

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import org.koin.android.ext.android.inject
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ActivityRootBinding
import ru.practicum.android.diploma.filter.domain.api.SettingsInteractor
import ru.practicum.android.diploma.filter.domain.models.Area
import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.filter.domain.models.Industry
import ru.practicum.android.diploma.filter.domain.models.WriteRequest

class RootActivity : AppCompatActivity() {

    private val settingsInteractor by inject<SettingsInteractor>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

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

        settingsInteractor.write(
            WriteRequest.WriteArea(
                Area(
                    "example",
                    "example"
                )
            )
        )
        settingsInteractor.write(
            WriteRequest.WriteSalary(100000)
        )
        settingsInteractor.write(
            WriteRequest.WriteCountry(
                Country(
                    "id",
                    "name"
                )
            )
        )
        settingsInteractor.write(
            WriteRequest.WriteIndustry(
                Industry(
                    "id",
                    "industry"
                )
            )
        )
        Log.d("RootActivity", "settings ${settingsInteractor.read()}")
    }
}


