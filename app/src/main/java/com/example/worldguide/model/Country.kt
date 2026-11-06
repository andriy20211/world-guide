package com.example.worldguide.model

import androidx.annotation.DrawableRes

data class Name(val official: String, val common: String)

data class Flags(
    val png: String = "",
    @DrawableRes val localDrawableId: Int? = null
)

data class Country(
    val id: String,
    val name: Name,
    val capital: List<String>,
    val population: Long,
    val region: String,
    val subregion: String,
    val area: Double,
    val languages: List<String>,
    val currencies: List<String>,
    val flags: Flags
)