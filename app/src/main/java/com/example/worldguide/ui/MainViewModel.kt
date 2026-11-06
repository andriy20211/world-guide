package com.example.worldguide.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.worldguide.data.UserPreferencesRepository
import com.example.worldguide.model.MainUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Використовуємо AndroidViewModel для отримання контексту додатку
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserPreferencesRepository(application)

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        // Підписуємося на Flow із сховища DataStore для автооновлення головного екрана
        viewModelScope.launch {
            repository.sortOrderFlow.collect { sortOrder ->
                _uiState.update { currentState ->
                    currentState.copy(sortOrder = sortOrder)
                }
            }
        }
    }

    // Зміна обраного регіону
    fun selectRegion(region: String) {
        _uiState.update { currentState ->
            currentState.copy(selectedRegion = region)
        }
    }

    // Запис нового налаштування у DataStore (suspend-функція викликається у viewModelScope)
    fun setSortOrder(sortOrder: String) {
        viewModelScope.launch {
            repository.saveSortOrder(sortOrder)
        }
    }
}