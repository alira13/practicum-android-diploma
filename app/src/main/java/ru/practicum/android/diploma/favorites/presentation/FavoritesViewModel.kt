package ru.practicum.android.diploma.favorites.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.search.domain.models.VacancyPreview

class FavoritesViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<FavoritesUiState>(FavoritesUiState.Default)
    val uiStateFlow = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            while (true) {
                _uiState.value = FavoritesUiState.Default
                delay(1000L)
                _uiState.value = FavoritesUiState.Empty
                delay(1000L)
                _uiState.value = FavoritesUiState.Failure
                delay(1000L)
                _uiState.value = FavoritesUiState.Content(
                getFavList())
                delay(10000L)
            }
        }
    }

    private fun getFavList():List<VacancyPreview>{
        return listOf(
            VacancyPreview(
                null.toString(),
                null,
                "Мегамозг, возраст 25 лет, опыт 15",
                "Сказошный наниматель",
                "ну где-то около 20 000 руб."
            ),
            VacancyPreview(
                null.toString(),
                null,
                "Аниматор для панд, Пекин",
                "Зоопарк",
                "15 бамбуковых листьев"
            ),
            VacancyPreview(
                null.toString(),
                null,
                "Тестировщик матрасов, Сонвиль",
                "Матрасофф и Ко",
                "5 подушек в месяц"
            ),
            VacancyPreview(
                null.toString(),
                null,
                "Упаковщик бананов",
                "ДельЭквадор",
                "з/п в бананах, оптом"
            ),
            VacancyPreview(
                null.toString(),
                null,
                "Мегамозг, возраст 25 лет, опыт 15",
                "Сказошный наниматель",
                "ну где-то около 20 000 руб."
            ),
            VacancyPreview(
                null.toString(),
                null,
                "Аниматор для панд, Пекин",
                "Зоопарк",
                "15 бамбуковых листьев"
            ),
            VacancyPreview(
                null.toString(),
                null,
                "Тестировщик матрасов, Сонвиль",
                "Матрасофф и Ко",
                "5 подушек в месяц"
            ),
            VacancyPreview(
                null.toString(),
                null,
                "Упаковщик бананов",
                "ДельЭквадор",
                "з/п в бананах, оптом"
            ),
            VacancyPreview(
                null.toString(),
                null,
                "Мегамозг, возраст 25 лет, опыт 15",
                "Сказошный наниматель",
                "ну где-то около 20 000 руб."
            ),
            VacancyPreview(
                null.toString(),
                null,
                "Аниматор для панд, Пекин",
                "Зоопарк",
                "15 бамбуковых листьев"
            ),
            VacancyPreview(
                null.toString(),
                null,
                "Тестировщик матрасов, Сонвиль",
                "Матрасофф и Ко",
                "5 подушек в месяц"
            ),
            VacancyPreview(
                null.toString(),
                null,
                "Упаковщик бананов",
                "ДельЭквадор",
                "з/п в бананах, оптом"
            )
        )
    }
}
