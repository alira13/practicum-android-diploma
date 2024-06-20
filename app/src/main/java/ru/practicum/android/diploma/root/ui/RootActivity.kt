package ru.practicum.android.diploma.root.ui

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ActivityRootBinding

class RootActivity : AppCompatActivity() {
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
    }
}
/*settingsInteractor.write(
    WriteRequest.WriteArea(
        Area(
            "example",
            "example"
        )
    )
)
val salary = 100000
settingsInteractor.write(
    WriteRequest.WriteSalary(salary)
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
Log.d("RootActivity", "settings ${settingsInteractor.read()}")*/
