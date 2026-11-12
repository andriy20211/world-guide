package com.example.worldguide.model

import com.example.worldguide.data.UserPreferencesRepository

data class MainUiState(
    val selectedRegion: String = "Всі",
    val availableRegions: List<String> = listOf("Всі", "Europe", "Asia", "Americas", "Africa", "Oceania"),
    val countries: List<Country> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null, // Поле помилки (null = помилки немає)
    val sortOrder: String = UserPreferencesRepository.SORT_BY_NAME
) {
    val filteredCountries: List<Country>
        get() {
            return when (sortOrder) {
                UserPreferencesRepository.SORT_BY_POPULATION -> countries.sortedByDescending { it.population }
                else -> countries.sortedBy { it.name.common }
            }
        }
}