package com.example.worldguide.ui

import androidx.lifecycle.ViewModel
import com.example.worldguide.model.MainUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {

    // Приватний стан для запису (закритий від змін ззовні)
    private val _uiState = MutableStateFlow(MainUiState())

    // Публічний стан тільки для читання
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    // Функція-подія для зміни стану через copy()
    fun selectRegion(region: String) {
        _uiState.update { currentState ->
            currentState.copy(selectedRegion = region)
        }
    }
}