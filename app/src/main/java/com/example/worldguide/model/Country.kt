package com.example.worldguide.model

import androidx.annotation.DrawableRes

// Вкладені класи під структуру REST API v3.1
data class NativeName(val official: String)
data class Name(val official: String, val common: String)
data class Flags(
    val png: String = "",
    @DrawableRes val localDrawableId: Int? = null // Для ЛР2 використовуємо локальний ресурс
)

// Модель країни
data class Country(
    val name: Name,
    val capital: List<String>,
    val population: Long,
    val region: String,
    val flags: Flags
)

// Список з 3 об'єктів окремо від верстки
val sampleCountries = listOf(
    Country(
        name = Name(official = "Ukraine", common = "Україна"),
        capital = listOf("Київ"),
        population = 38000000L,
        region = "Europe",
        flags = Flags(localDrawableId = com.example.worldguide.R.drawable.flag_ua)
    ),
    Country(
        name = Name(official = "Federal Republic of Germany", common = "Німеччина"),
        capital = listOf("Берлін"),
        population = 83200000L,
        region = "Europe",
        flags = Flags(localDrawableId = com.example.worldguide.R.drawable.flag_de)
    ),
    Country(
        name = Name(official = "Japan", common = "Японія"),
        capital = listOf("Токіо"),
        population = 125100000L,
        region = "Asia",
        flags = Flags(localDrawableId = com.example.worldguide.R.drawable.flag_jp)
    )
)