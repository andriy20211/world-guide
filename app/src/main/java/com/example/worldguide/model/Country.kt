package com.example.worldguide.model

import androidx.annotation.DrawableRes

data class Name(val official: String, val common: String)
data class Flags(
    val png: String = "",
    @DrawableRes val localDrawableId: Int? = null
)

data class Country(
    val id: String, // Унікальний ідентифікатор (наприклад, "ukr", "deu")
    val name: Name,
    val capital: List<String>,
    val population: Long,
    val region: String,
    val subregion: String, // Додаткове поле для деталей
    val area: Double,       // Додаткове поле для деталей (площа)
    val languages: List<String>, // Мови
    val currencies: List<String>, // Валюта
    val flags: Flags
)

val sampleCountries = listOf(
    Country(
        id = "ukr",
        name = Name(official = "Ukraine", common = "Україна"),
        capital = listOf("Київ"),
        population = 38000000L,
        region = "Europe",
        subregion = "Eastern Europe",
        area = 603628.0,
        languages = listOf("Українська"),
        currencies = listOf("UAH (Гривня)"),
        flags = Flags(localDrawableId = com.example.worldguide.R.drawable.flag_ua)
    ),
    Country(
        id = "deu",
        name = Name(official = "Federal Republic of Germany", common = "Німеччина"),
        capital = listOf("Берлін"),
        population = 83200000L,
        region = "Europe",
        subregion = "Western Europe",
        area = 357022.0,
        languages = listOf("Німецька"),
        currencies = listOf("EUR (Євро)"),
        flags = Flags(localDrawableId = com.example.worldguide.R.drawable.flag_de)
    ),
    Country(
        id = "jpn",
        name = Name(official = "Japan", common = "Японія"),
        capital = listOf("Токіо"),
        population = 125100000L,
        region = "Asia",
        subregion = "Eastern Asia",
        area = 377975.0,
        languages = listOf("Японська"),
        currencies = listOf("JPY (Єна)"),
        flags = Flags(localDrawableId = com.example.worldguide.R.drawable.flag_jp)
    ),
    Country(
        id = "can",
        name = Name(official = "Canada", common = "Канада"),
        capital = listOf("Оттава"),
        population = 38200000L,
        region = "Americas",
        subregion = "North America",
        area = 9984670.0,
        languages = listOf("Англійська", "Французька"),
        currencies = listOf("CAD (Канадський долар)"),
        flags = Flags(localDrawableId = com.example.worldguide.R.drawable.flag_ca)
    ),
    Country(
        id = "bra",
        name = Name(official = "Federative Republic of Brazil", common = "Бразилія"),
        capital = listOf("Бразиліа"),
        population = 214300000L,
        region = "Americas",
        subregion = "South America",
        area = 8515767.0,
        languages = listOf("Португальська"),
        currencies = listOf("BRL (Бразильський реал)"),
        flags = Flags(localDrawableId = com.example.worldguide.R.drawable.flag_br)
    ),
    Country(
        id = "aus",
        name = Name(official = "Commonwealth of Australia", common = "Австралія"),
        capital = listOf("Канберра"),
        population = 25600000L,
        region = "Oceania",
        subregion = "Australia and New Zealand",
        area = 7692024.0,
        languages = listOf("Англійська"),
        currencies = listOf("AUD (Австралійський долар)"),
        flags = Flags(localDrawableId = com.example.worldguide.R.drawable.flag_au)
    )
)