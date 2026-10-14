package com.example.worldguide.model

import androidx.annotation.DrawableRes

data class Name(val official: String, val common: String)
data class Flags(
    val png: String = "",
    @DrawableRes val localDrawableId: Int? = null
)

data class Country(
    val name: Name,
    val capital: List<String>,
    val population: Long,
    val region: String, // Поле для фільтрації
    val flags: Flags
)

// Список із 6 об'єктів з різними регіонами
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
    ),
    Country(
        name = Name(official = "Canada", common = "Канада"),
        capital = listOf("Оттава"),
        population = 38200000L,
        region = "Americas",
        flags = Flags(localDrawableId = com.example.worldguide.R.drawable.flag_jp)
    ),
    Country(
        name = Name(official = "Federative Republic of Brazil", common = "Бразилія"),
        capital = listOf("Бразиліа"),
        population = 214300000L,
        region = "Americas",
        flags = Flags(localDrawableId = com.example.worldguide.R.drawable.flag_ua)
    ),
    Country(
        name = Name(official = "Commonwealth of Australia", common = "Австралія"),
        capital = listOf("Канберра"),
        population = 25600000L,
        region = "Oceania",
        flags = Flags(localDrawableId = com.example.worldguide.R.drawable.flag_de)
    )
)