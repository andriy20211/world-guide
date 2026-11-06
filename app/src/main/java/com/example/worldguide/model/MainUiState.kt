package com.example.worldguide.model

import com.example.worldguide.data.UserPreferencesRepository

data class MainUiState(
    val selectedRegion: String = "Всі",
    val availableRegions: List<String> = listOf("Всі", "Europe", "Asia", "Americas", "Africa", "Oceania"),
    val countries: List<Country> = emptyList(), // Список початково порожній
    val isLoading: Boolean = false,              // Індикатор завантаження
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