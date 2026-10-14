package com.example.worldguide.model

// Стан екрана описується окремим data class з полями val (immutable)
data class MainUiState(
    val selectedRegion: String = "Всі",
    val availableRegions: List<String> = listOf("Всі", "Europe", "Asia", "Americas", "Africa", "Oceania"),
    val countries: List<Country> = sampleCountries
) {
    // Відфільтрований список обчислюється на основі обраного регіону
    val filteredCountries: List<Country>
        get() = if (selectedRegion == "Всі") {
            countries
        } else {
            countries.filter { it.region.equals(selectedRegion, ignoreCase = true) }
        }
}