package ru.practicum.android.diploma.di

import android.content.Context.MODE_PRIVATE
import androidx.room.Room
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.favorites.data.converters.FavoriteConverter
import ru.practicum.android.diploma.favorites.data.db.AppDatabase
import ru.practicum.android.diploma.filter.data.api.SettingsHandler
import ru.practicum.android.diploma.filter.data.converter.FilterConverter
import ru.practicum.android.diploma.filter.data.local.SettingsHandlerImpl
import ru.practicum.android.diploma.search.data.VacancyConverter
import ru.practicum.android.diploma.search.data.api.HHApiService
import ru.practicum.android.diploma.search.data.api.NetworkClient
import ru.practicum.android.diploma.search.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.share.data.ExternalNavigator
import ru.practicum.android.diploma.vacancy.data.DetailsConverter

const val BASE_URL = "https://api.hh.ru/"
private const val VACANCY_CATCHER_SHARED_PREFS = "vacancy_catcher_shared_prefs"
val dataModule = module {

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    factory { VacancyConverter(androidContext()) }

    factory {
        DetailsConverter(
            context = androidContext(),
            favoriteRepository = get()
        )
    }

    single {
        ExternalNavigator(
            context = androidContext()
        )
    }

    single<NetworkClient> {
        RetrofitNetworkClient(
            apiService = get()
        )
    }

    single<HHApiService> {
        val client = OkHttpClient.Builder().addInterceptor(
            HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            }
        ).build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(HHApiService::class.java)
    }

    single { FavoriteConverter() }

    single {
        androidContext().getSharedPreferences(VACANCY_CATCHER_SHARED_PREFS, MODE_PRIVATE)
    }

    factory { Gson() }

    factory { FilterConverter() }

    single<SettingsHandler> { SettingsHandlerImpl(get(), get()) }

}
