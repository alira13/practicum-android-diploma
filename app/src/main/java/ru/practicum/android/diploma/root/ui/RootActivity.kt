package ru.practicum.android.diploma.root.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.search.data.api.NetworkClient
import ru.practicum.android.diploma.search.data.dto.VacancySearchRequest

class RootActivity : AppCompatActivity() {

    //private val networkClient by inject<NetworkClient>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)
        Log.d(TAG, "response")
        // Пример использования access token для HeadHunter API
        networkRequestExample(accessToken = BuildConfig.HH_ACCESS_TOKEN)
    }

    private fun networkRequestExample(accessToken: String) {
        /*lifecycleScope.launch {
            val response = networkClient.doRequest(VacancySearchRequest("разработчик"))
            Log.d(TAG, "response $response")
        }*/
    }

    companion object {
        const val TAG = "RootActivity"
    }

}
