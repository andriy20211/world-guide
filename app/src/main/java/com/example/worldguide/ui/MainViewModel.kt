package com.example.worldguide.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.worldguide.data.UserPreferencesRepository
import com.example.worldguide.data.remote.NetworkClient
import com.example.worldguide.data.remote.toDomain
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

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        // Підписка на налаштування сортування
        viewModelScope.launch {
            repository.sortOrderFlow.collect { sortOrder ->
                _uiState.update { it.copy(sortOrder = sortOrder) }
            }
        }

        // Перший запит при відкритті екрана
        loadCountries(_uiState.value.selectedRegion)
    }

    // Виклик запиту при зміні вибору регіону
    fun selectRegion(region: String) {
        _uiState.update { it.copy(selectedRegion = region) }
        loadCountries(region)
    }

    private fun loadCountries(region: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val dtos = if (region == "Всі") {
                    apiService.getAllCountries()
                } else {
                    apiService.getCountriesByRegion(region)
                }
                val domainCountries = dtos.map { it.toDomain() }
                _uiState.update { it.copy(countries = domainCountries, isLoading = false) }
            } catch (e: Exception) {
                // Достатньо, щоб застосунок не падав
                _uiState.update { it.copy(countries = emptyList(), isLoading = false) }
            }
        }
    }

    fun setSortOrder(sortOrder: String) {
        viewModelScope.launch {
            repository.saveSortOrder(sortOrder)
        }
    }
}