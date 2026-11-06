package com.example.worldguide.model

import com.example.worldguide.data.UserPreferencesRepository

data class MainUiState(
    val selectedRegion: String = "Всі",
    val availableRegions: List<String> = listOf("Всі", "Europe", "Asia", "Americas", "Africa", "Oceania"),
    val countries: List<Country> = sampleCountries,
    val sortOrder: String = UserPreferencesRepository.SORT_BY_NAME // Спосіб сортування
) {
    // Відфільтрований та відсортований список обчислюється на основі стану
    val filteredCountries: List<Country>
        get() {
            val filtered = if (selectedRegion == "Всі") {
                countries
            } else {
                countries.filter { it.region.equals(selectedRegion, ignoreCase = true) }
            }

            return when (sortOrder) {
                UserPreferencesRepository.SORT_BY_POPULATION -> filtered.sortedByDescending { it.population }
                else -> filtered.sortedBy { it.name.common }
            }
        }
}