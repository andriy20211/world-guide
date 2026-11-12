package com.example.worldguide.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Делегат preferencesDataStore оголошується один раз в окремому файлі на рівні Context
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_settings")

class UserPreferencesRepository(private val context: Context) {

    companion object {
        // Ключ для збереження способу сортування
        val SORT_ORDER_KEY = stringPreferencesKey("sort_order")

        // Значення за замовчуванням для першого запуску
        const val SORT_BY_NAME = "NAME"
        const val SORT_BY_POPULATION = "POPULATION"
    }

    // Читання налаштувань як Flow із значенням за замовчуванням (SORT_BY_NAME)
    val sortOrderFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[SORT_ORDER_KEY] ?: SORT_BY_NAME
    }

    // Suspend-функція для запису обраного параметра у сховище
    suspend fun saveSortOrder(sortOrder: String) {
        context.dataStore.edit { preferences ->
            preferences[SORT_ORDER_KEY] = sortOrder
        }
    }
}