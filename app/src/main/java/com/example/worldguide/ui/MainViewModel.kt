package com.example.worldguide.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.worldguide.data.UserPreferencesRepository
import com.example.worldguide.data.remote.NetworkClient
import com.example.worldguide.data.remote.toDomain
import com.example.worldguide.data.remote.toNetworkError
import com.example.worldguide.model.Country
import com.example.worldguide.model.MainUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserPreferencesRepository(application)
    private val apiService = NetworkClient.apiService

    // Кеш завантажених країн по регіонах (запобігає повторним запитам при поверненні з деталей)
    private val loadedCountriesCache = mutableMapOf<String, List<Country>>()

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.sortOrderFlow.collect { sortOrder ->
                _uiState.update { it.copy(sortOrder = sortOrder) }
            }
        }
        loadCountries(_uiState.value.selectedRegion)
    }

    fun selectRegion(region: String) {
        if (_uiState.value.selectedRegion == region && loadedCountriesCache.containsKey(region)) return
        _uiState.update { it.copy(selectedRegion = region) }
        loadCountries(region)
    }

    fun retry() {
        loadedCountriesCache.remove(_uiState.value.selectedRegion)
        loadCountries(_uiState.value.selectedRegion)
    }

    private fun loadCountries(region: String) {
        // Беремо дані з кешу, якщо вони вже завантажені
        loadedCountriesCache[region]?.let { cachedCountries ->
            _uiState.update {
                it.copy(
                    countries = cachedCountries,
                    isLoading = false,
                    errorMessage = null
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val dtos = if (region == "Всі") {
                    apiService.getAllCountries()
                } else {
                    apiService.getCountriesByRegion(region)
                }
                // distinctBy { it.id } додатково гарантує 100% унікальність ключів у LazyColumn
                val domainCountries = dtos.map { it.toDomain() }.distinctBy { it.id }

                loadedCountriesCache[region] = domainCountries

                _uiState.update {
                    it.copy(
                        countries = domainCountries,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            } catch (e: Throwable) {
                val netError = e.toNetworkError()
                _uiState.update {
                    it.copy(
                        countries = emptyList(),
                        isLoading = false,
                        errorMessage = netError.userMessage
                    )
                }
            }
        }
    }

    fun setSortOrder(sortOrder: String) {
        viewModelScope.launch {
            repository.saveSortOrder(sortOrder)
        }
    }
}