package ru.practicum.android.diploma.di

import androidx.room.Room
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.favorites.data.converters.VacancyConverterDB
import ru.practicum.android.diploma.favorites.data.db.AppDatabase
import ru.practicum.android.diploma.search.data.VacancyConverter
import ru.practicum.android.diploma.search.data.api.HHApiService
import ru.practicum.android.diploma.search.data.api.NetworkClient
import ru.practicum.android.diploma.search.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.share.data.ExternalNavigator
import ru.practicum.android.diploma.vacancy.data.DetailsConverter

const val BASE_URL = "https://api.hh.ru/"

val dataModule = module{

    factory { DetailsConverter() }

    single { VacancyConverterDB() }

    factory { VacancyConverter(androidContext()) }

    single {
        ExternalNavigator(
            context = get()
        )
    }

    single<NetworkClient> {
        RetrofitNetworkClient(
            apiService = get()
        )
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .fallbackToDestructiveMigration()
            .build()
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
}
