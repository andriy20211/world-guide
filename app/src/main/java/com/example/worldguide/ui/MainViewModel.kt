package com.example.worldguide.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.worldguide.data.UserPreferencesRepository
import com.example.worldguide.data.remote.NetworkClient
import com.example.worldguide.data.remote.toDomain
import com.example.worldguide.data.remote.toNetworkError
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
        viewModelScope.launch {
            repository.sortOrderFlow.collect { sortOrder ->
                _uiState.update { it.copy(sortOrder = sortOrder) }
            }
        }
        loadCountries(_uiState.value.selectedRegion)
    }

    fun selectRegion(region: String) {
        _uiState.update { it.copy(selectedRegion = region) }
        loadCountries(region)
    }

    // Повторний запит при натисканні «Спробувати ще»
    fun retry() {
        loadCountries(_uiState.value.selectedRegion)
    }

    private fun loadCountries(region: String) {
        viewModelScope.launch {
            // Включаємо завантаження та скидаємо попередню помилку
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val dtos = if (region == "Всі") {
                    apiService.getAllCountries()
                } else {
                    apiService.getCountriesByRegion(region)
                }
                val domainCountries = dtos.map { it.toDomain() }

                _uiState.update {
                    it.copy(
                        countries = domainCountries,
                        isLoading = false,
                        errorMessage = null // Помилки немає, успішно завантажено
                    )
                }
            } catch (e: Throwable) {
                val netError = e.toNetworkError()
                _uiState.update {
                    it.copy(
                        countries = emptyList(),
                        isLoading = false,
                        errorMessage = netError.userMessage // Записуємо зрозумілий текст помилки
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